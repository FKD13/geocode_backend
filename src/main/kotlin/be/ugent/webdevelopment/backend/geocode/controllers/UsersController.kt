package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
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

    @PutMapping(value = ["/{id}"])
    fun update(@PathVariable id: Int, @RequestBody resource: UsersWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.update(id, resource)
    }

    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable id: Int,
               response: HttpServletResponse, request: HttpServletRequest) {
        if (service.deleteById(id) != 1){
            throw IllegalArgumentException("ID was not found so User could not be deleted")
        }
    }

}