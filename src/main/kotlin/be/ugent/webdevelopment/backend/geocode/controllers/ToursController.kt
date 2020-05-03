package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.TourWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ToursStatistics
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.ToursService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/tours")
@JsonView(View.PublicDetail::class)
class ToursController(
        val toursService: ToursService,
        val jwtAuthenticator: JWTAuthenticator
) {

    @GetMapping
    fun getTours(request: HttpServletRequest, response: HttpServletResponse): List<Tour> {
        return toursService.getTours()
    }

    @PostMapping
    fun create(@RequestBody resource: TourWrapper, request: HttpServletRequest, response: HttpServletResponse): UUID {
        return toursService.createTour(resource, jwtAuthenticator.tryAuthenticate(request))
    }

    @GetMapping(value = ["/{secretId}"])
    fun getTourById(@PathVariable secretId: UUID, request: HttpServletRequest, response: HttpServletResponse): Tour {
        return toursService.getTourById(secretId)
    }

    @PatchMapping(value = ["/{secretId}"])
    fun update(@PathVariable secretId: UUID, @RequestBody resource: TourWrapper,
               request: HttpServletRequest, response: HttpServletResponse) {
        toursService.updateTour(secretId, resource)
    }

    @DeleteMapping(value = ["/{secretId}"])
    fun delete(@PathVariable secretId: UUID, request: HttpServletRequest, response: HttpServletResponse) {
        toursService.deleteTour(secretId, jwtAuthenticator.tryAuthenticate(request))
    }

    @GetMapping("/{secretId}/statistics")
    fun statistics(@PathVariable secretId: UUID, request: HttpServletRequest, response: HttpServletResponse): ToursStatistics {
        return toursService.statistics(secretId)
    }


}