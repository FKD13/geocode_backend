package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedUserWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/users")
class UsersController(val service: UsersService) {

    @GetMapping
    @JsonView(View.List::class)
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<ExtendedUserWrapper> {
        return service.findAll()
    }

    @GetMapping(value = ["/{id}"])
    @JsonView(View.PublicDetail::class)
    fun findById(@PathVariable id: Int,
                 response: HttpServletResponse, request: HttpServletRequest): ExtendedUserWrapper {
        return service.findById(id)
    }

}