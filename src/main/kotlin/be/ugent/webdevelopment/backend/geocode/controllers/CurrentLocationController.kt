package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.CurrentLocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.TourWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ToursStatistics
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import be.ugent.webdevelopment.backend.geocode.services.CurrentLocationService
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.ToursService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/currentlocation")
@JsonView(View.PublicDetail::class)
class CurrentLocationController(
        val currentLocationService: CurrentLocationService
) {

    @GetMapping
    fun getCurrentLocation(request: HttpServletRequest, response: HttpServletResponse): CurrentLocationWrapper {
        return currentLocationService.getCurrentLocation(request.remoteAddr)
    }
}