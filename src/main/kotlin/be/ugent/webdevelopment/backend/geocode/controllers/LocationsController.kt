package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.RatingsWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
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
        val ratingsService: RatingsService
) {

    @GetMapping
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<Location> {
        return service.findAll()
    }

    @GetMapping(value = ["/{secret_id}"])
    fun findById(@PathVariable secret_id: UUID,
                 response: HttpServletResponse, request: HttpServletRequest): LocationsWrapper {
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
        if (jwtService.tryAuthenticate(request).id != service.findBySecretId(secret_id).creatorId.get())
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
    fun visitLocation(@PathVariable visitSecret: UUID) {
        //TODO
    }

    @GetMapping(value = ["/visits/{visitSecret}"])
    fun getLocationByVisitSecret(@PathVariable visitSecret: UUID) {
        //TODO
    }

    @GetMapping(value = ["/{secretId}/visits"])
    fun getVisitsBySecretId(@PathVariable secretId: UUID) {
        //TODO
    }

    //------------------------------------------------------------------------------------------------------------------
    // Ratings

    @GetMapping(value = ["/{secretId}/ratings"])
    @JsonView(View.List::class)
    fun getRatingsByLocation(@PathVariable secretId: UUID) {
        ratingsService.getRatingsByLocation(secretId)
    }

    @PostMapping(value = ["/{secretId}/ratings"])
    fun addRating(@PathVariable secretId: UUID, @RequestBody ratingsWrapper: RatingsWrapper,
                  request: HttpServletRequest, response: HttpServletResponse) {
        val user = jwtService.tryAuthenticate(request)
        ratingsService.addRating(user, secretId, ratingsWrapper)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Reports

    @GetMapping(value = ["/{secretId}/reports"])
    fun getReportsByLocation(@PathVariable secretId: UUID) {
        //TODO
    }

    @PostMapping(value = ["/{secretId}/reports"])
    fun addReports(@PathVariable secretId: UUID, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    //------------------------------------------------------------------------------------------------------------------
    // Reports

    @GetMapping(value = ["/{secretId}/comments"])
    fun getCommentsByLocation(@PathVariable secretId: UUID) {
        //TODO
    }

    @PostMapping(value = ["/{secretId}/comments"])
    fun addComments(@PathVariable secretId: UUID, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
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