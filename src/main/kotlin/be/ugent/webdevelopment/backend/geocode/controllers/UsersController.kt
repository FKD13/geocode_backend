package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.User
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
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<User> {
        return service.findAll()
    }

    @GetMapping(value = ["/{id}"])
    @JsonView(View.PublicDetail::class)
    fun findById(@PathVariable id: Int,
                 response: HttpServletResponse, request: HttpServletRequest): User {
        return service.findById(id)
    }

    @DeleteMapping(value = ["/{id}"])
    @JsonView(View.AdminDetail::class)
    fun deleteById(@PathVariable id: Int,
                 response: HttpServletResponse, request: HttpServletRequest) {
        service.checkAdmin(request)
        service.deleteById(id)
    }

    @PatchMapping(value = ["/{id}"])
    @JsonView(View.AdminDetail::class)
    fun updateById(
            @PathVariable id: Int,
            @RequestBody resource: UserWrapper,
            response: HttpServletResponse,
            request: HttpServletRequest
    ) {
        service.checkAdmin(request)
        service.updateById(id, resource)
    }
}