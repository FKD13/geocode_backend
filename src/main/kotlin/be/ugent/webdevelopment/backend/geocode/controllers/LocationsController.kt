package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedLocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.RatingsWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import be.ugent.webdevelopment.backend.geocode.services.VisitsService
import be.ugent.webdevelopment.backend.geocode.services.RatingsService
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
        val visitsService: VisitsService
) {

    @GetMapping
    @JsonView(View.PublicDetail::class)
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<ExtendedLocationWrapper> {
        return service.findAll()
    }

    @GetMapping(value = ["/{secret_id}"])
    @JsonView(View.PublicDetail::class)
    fun findById(@PathVariable secret_id: UUID,
                 response: HttpServletResponse, request: HttpServletRequest): Location {
        return service.findBySecretId(secret_id)
    }

    @PostMapping
    fun create(@RequestBody resource: LocationsWrapper,
               response: HttpServletResponse, request: HttpServletRequest): UUID {
        resource.creatorId = Optional.of(jwtService.tryAuthenticate(request).id)
        return service.create(resource)
    }

    @PatchMapping(value = ["/{secret_id}"])
    fun update(@PathVariable secret_id: UUID, @RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        if (jwtService.tryAuthenticate(request).id != service.findBySecretId(secret_id).creator.id)
            throw GenericException("The currently logged in user did not create this location and can therefor not edit it.")
        service.update(secret_id, resource)
    }

    @DeleteMapping(value = ["/{secret_id}"])
    fun delete(@PathVariable secret_id: UUID,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteById(jwtService.tryAuthenticate(request), secret_id)
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
    fun getLocationByVisitSecret(@PathVariable visitSecret: UUID) : Location {
        return visitsService.getByVisitSecret(visitSecret)
    }

    @GetMapping(value = ["/{secret_id}/visits"])
    @JsonView(View.PublicDetail::class)
    fun getVisitsBySecretId(@PathVariable secret_id: UUID) : List<CheckIn> {
        return visitsService.getVisitsBySecretId(secret_id)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Ratings

    @GetMapping(value = ["/{secret_id}/ratings"])
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

    @GetMapping(value = ["/{secret_id}/reports"])
    @JsonView(View.AdminDetail::class)
    fun getReportsByLocation(@PathVariable secret_id: UUID) {
        //TODO
    }

    @PostMapping(value = ["/{secret_id}/reports"])
    fun addReports(@PathVariable secret_id: UUID, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    //------------------------------------------------------------------------------------------------------------------
    // Reports

    @GetMapping(value = ["/{secret_id}/comments"])
    @JsonView(View.PublicDetail::class)
    fun getCommentsByLocation(@PathVariable secret_id: UUID) {
        //TODO
    }

    @PostMapping(value = ["/{secret_id}/comments"])
    fun addComments(@PathVariable secret_id: UUID, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping("/{secret_id}/qrcode")
    fun getQrcode(
            @RequestParam("frontend") frontendUrl: String,
            @RequestParam("size") size: Int,
            request: HttpServletRequest,
            response: HttpServletResponse): BufferedImage {
        return BufferedImage(0, 0, 0)
    }
}