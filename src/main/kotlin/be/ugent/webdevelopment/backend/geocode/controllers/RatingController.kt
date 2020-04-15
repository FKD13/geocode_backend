package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.RatingsWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.RatingsService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/ratings")
class RatingController(val ratingsService: RatingsService, val jwtAuthenticator: JWTAuthenticator) {

    @GetMapping("/{ratingId}")
    @JsonView(View.PublicDetail::class)
    fun getRatingById(@PathVariable ratingId: Int): LocationRating {
        return ratingsService.getRatingById(ratingId)
    }

    @PatchMapping("/{ratingId}")
    fun updateRatingById(@PathVariable ratingId: Int, @RequestBody ratingsWrapper: RatingsWrapper,
                         request: HttpServletRequest, response: HttpServletResponse) {
        val user = jwtAuthenticator.tryAuthenticate(request)
        ratingsService.updateRatingById(ratingId, ratingsWrapper, user)
    }

    @DeleteMapping("/{ratingId}")
    fun deleteRatingById(@PathVariable ratingId: Int, request: HttpServletRequest, response: HttpServletResponse) {
        val user = jwtAuthenticator.tryAuthenticate(request)
        ratingsService.deleteRatingById(ratingId, user)
    }
}