package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/users")
class UsersController(val service: UsersService): Controller<UsersWrapper>{

    @GetMapping
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<UsersWrapper> {
        return service.findAll()
    }

    @GetMapping(value = ["/{id}"])
    fun findById(@PathVariable id: Int,
                 response: HttpServletResponse, request: HttpServletRequest): UsersWrapper {
        return service.findById(id)
    }

}