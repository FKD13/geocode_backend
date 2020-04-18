package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.*
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.*
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.services.*
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.awt.image.BufferedImage
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/locations", produces = ["application/ld+json"])
class LocationsController(
        val service: LocationsService,
        val jwtService: JWTAuthenticator,
        val ratingsService: RatingsService,
        val commentsService: CommentsService,
        val visitsService: VisitsService,
        val qrCodeService: QRCodeService,
        val reportService: ReportService
) {


    @GetMapping
    @JsonView(View.List::class)
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<ExtendedLocationWrapper> {
        return service.findAll()
    }

    @GetMapping(value = ["/{secretId}"])
    @JsonView(View.PublicDetail::class)
    fun findById(@PathVariable secretId: UUID,
                 response: HttpServletResponse, request: HttpServletRequest): ExtendedLocationWrapper {
        return service.findBySecretId(secretId)
    }

    @PostMapping
    @JsonView(View.PrivateDetail::class)
    fun create(@RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest): Location {
        resource.creatorId = Optional.of(jwtService.tryAuthenticate(request).id)
        return service.create(resource)
    }

    @PatchMapping(value = ["/{secretId}"])
    fun update(@PathVariable secretId: UUID, @RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        if (jwtService.tryAuthenticate(request).id != service.findBySecretId(secretId).loc.creator.id)
            throw GenericException("The currently logged in user did not create this location and can therefor not edit it.")
        service.update(secretId, resource)
    }

    @DeleteMapping(value = ["/{secretId}"])
    fun delete(@PathVariable secretId: UUID,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteById(jwtService.tryAuthenticate(request), secretId)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Visits

    @PostMapping(value = ["/visits/{visitSecret}"])
    @JsonView(View.Id::class)
    fun visitLocation(@PathVariable visitSecret: UUID,
                      response: HttpServletResponse, request: HttpServletRequest): ExtendedLocationWrapper {
        return visitsService.visit(jwtService.tryAuthenticate(request), visitSecret)
    }

    @GetMapping(value = ["/visits/{visitSecret}"])
    @JsonView(View.PublicDetail::class)
    fun getLocationByVisitSecret(@PathVariable visitSecret: UUID): ExtendedLocationWrapper {
        return visitsService.getByVisitSecret(visitSecret)
    }

    @GetMapping(value = ["/{secretId}/visits"])
    @JsonView(View.List::class)
    fun getVisitsBySecretId(@PathVariable secretId: UUID): List<CheckIn> {
        return visitsService.getVisitsBySecretId(secretId)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Ratings

    @GetMapping(value = ["/{secretId}/ratings"])
    @JsonView(View.List::class)
    fun getRatingsByLocation(@PathVariable secretId: UUID): List<LocationRating> {
        return ratingsService.getRatingsByLocation(secretId)
    }

    @PostMapping(value = ["/{secretId}/ratings"])
    @JsonView(View.PrivateDetail::class)
    fun addRating(@PathVariable secretId: UUID, @RequestBody ratingsWrapper: RatingsWrapper,
                  request: HttpServletRequest, response: HttpServletResponse): LocationRating {
        val user = jwtService.tryAuthenticate(request)
        return ratingsService.addRating(user, secretId, ratingsWrapper)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Reports

    @GetMapping(value = ["/{secretId}/reports"])
    @JsonView(View.AdminDetail::class)
    fun getReportsByLocation(@PathVariable secretId: UUID,
                             request: HttpServletRequest, response: HttpServletResponse): List<Report> {
        reportService.checkAdmin(request)
        return reportService.getByLocation(secretId)
    }

    @PostMapping(value = ["/{secretId}/reports"])
    @JsonView(View.AdminDetail::class)
    fun addReports(@PathVariable secretId: UUID, @RequestBody reportsWrapper: ReportsWrapper,
                   request: HttpServletRequest, response: HttpServletResponse): Report {
        reportService.checkAdmin(request)
        return reportService.create(jwtService.tryAuthenticate(request), secretId, reportsWrapper)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Comments

    @GetMapping(value = ["/{secretId}/comments"])
    @JsonView(View.List::class)
    fun getCommentsByLocation(@PathVariable secretId: UUID): List<Comment> {
        return commentsService.getCommentsBySecretId(secretId)
    }

    @PostMapping(value = ["/{secretId}/comments"])
    @JsonView(View.PrivateDetail::class)
    fun addComments(@PathVariable secretId: UUID, @RequestBody comment: CommentWrapper,
                    request: HttpServletRequest, response: HttpServletResponse): Comment {
        return commentsService.createComment(jwtService.tryAuthenticate(request), secretId, comment)
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/{secretId}/qrcode", produces = ["application/jpeg"])
    fun getQrcode(
            @RequestParam("frontend") frontendUrl: String,
            @RequestParam("size") size: Int,
            @PathVariable("secretId") secretId: UUID,
            request: HttpServletRequest,
            response: HttpServletResponse): BufferedImage {
        val location = service.findBySecretId(secretId)
        if (jwtService.tryAuthenticate(request).id != location.loc.creator.id) {
            throw GenericException("This user did not create this location and can therefore not get a QR code for it.")
        }
        return qrCodeService.getQRCode(location.loc.visitSecret, frontendUrl, size)
    }
}