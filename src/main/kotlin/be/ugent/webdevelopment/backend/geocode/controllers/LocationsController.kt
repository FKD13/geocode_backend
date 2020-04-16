package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.*
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Comment
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.services.CommentsService
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import be.ugent.webdevelopment.backend.geocode.services.RatingsService
import be.ugent.webdevelopment.backend.geocode.services.VisitsService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.awt.image.BufferedImage
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/locations")
class LocationsController(
        val service: LocationsService,
        val jwtService: JWTAuthenticator,
        val ratingsService: RatingsService,
        val commentsService: CommentsService,
        val visitsService: VisitsService
) {

    @GetMapping
    @JsonView(View.PublicDetail::class)
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<ExtendedLocationWrapper> {
        return service.findAll()
    }

    @GetMapping(value = ["/{secretId}"])
    @JsonView(View.PublicDetail::class)
    fun findById(@PathVariable secretId: UUID,
                 response: HttpServletResponse, request: HttpServletRequest): Location {
        return service.findBySecretId(secretId)
    }

    @PostMapping
    fun create(@RequestBody resource: LocationsWrapper,
               response: HttpServletResponse, request: HttpServletRequest): UUID {
        resource.creatorId = Optional.of(jwtService.tryAuthenticate(request).id)
        return service.create(resource)
    }

    @PatchMapping(value = ["/{secretId}"])
    fun update(@PathVariable secretId: UUID, @RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        if (jwtService.tryAuthenticate(request).id != service.findBySecretId(secretId).creator.id)
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
    fun visitLocation(@PathVariable visitSecret: UUID,
                      response: HttpServletResponse, request: HttpServletRequest) {
        visitsService.visit(jwtService.tryAuthenticate(request), visitSecret)
    }

    @GetMapping(value = ["/visits/{visitSecret}"])
    @JsonView(View.PublicDetail::class)
    fun getLocationByVisitSecret(@PathVariable visitSecret: UUID): Location {
        return visitsService.getByVisitSecret(visitSecret)
    }

    @GetMapping(value = ["/{secretId}/visits"])
    @JsonView(View.PublicDetail::class)
    fun getVisitsBySecretId(@PathVariable secretId: UUID): List<CheckIn> {
        return visitsService.getVisitsBySecretId(secretId)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Ratings

    @GetMapping(value = ["/{secretId}/ratings"])
    @JsonView(View.PublicDetail::class)
    fun getRatingsByLocation(@PathVariable secretId: UUID): List<LocationRating> {
        return ratingsService.getRatingsByLocation(secretId)
    }

    @PostMapping(value = ["/{secretId}/ratings"])
    @JsonView(View.PublicDetail::class)
    fun addRating(@PathVariable secretId: UUID, @RequestBody ratingsWrapper: RatingsWrapper,
                  request: HttpServletRequest, response: HttpServletResponse): LocationRating {
        val user = jwtService.tryAuthenticate(request)
        return ratingsService.addRating(user, secretId, ratingsWrapper)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Reports

    @GetMapping(value = ["/{secretId}/reports"])
    @JsonView(View.AdminDetail::class)
    fun getReportsByLocation(@PathVariable secretId: UUID) {
        //TODO
    }

    @PostMapping(value = ["/{secretId}/reports"])
    fun addReports(@PathVariable secretId: UUID, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    //------------------------------------------------------------------------------------------------------------------
    // Comments

    @GetMapping(value = ["/{secretId}/comments"])
    @JsonView(View.PublicDetail::class)
    fun getCommentsByLocation(@PathVariable secretId: UUID): List<Comment> {
        return commentsService.getCommentsBySecretId(secretId)
    }

    @PostMapping(value = ["/{secretId}/comments"])
    fun addComments(@PathVariable secretId: UUID, @RequestBody comment: CommentWrapper,
                    request: HttpServletRequest, response: HttpServletResponse) {
        commentsService.createComment(jwtService.tryAuthenticate(request), secretId, comment)
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/{secretId}/qrcode")
    fun getQrcode(
            @RequestParam("frontend") frontendUrl: String,
            @RequestParam("size") size: Int,
            request: HttpServletRequest,
            response: HttpServletResponse): BufferedImage {
        return BufferedImage(0, 0, 0)
    }
}