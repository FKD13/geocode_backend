package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.Wrapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/ratings")
class RatingController : Controller<Wrapper> {

    @GetMapping("/{ratingId}")
    fun getRatingById(@PathVariable ratingId: Int) {
        //TODO
    }

    @PatchMapping("/{ratingId}")
    fun updateRatingById(@PathVariable ratingId: Int) {
        //TODO
    }

    @DeleteMapping("/{ratingId}")
    fun updateRatingById(@PathVariable ratingId: Int, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }
}