package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/user")
class UserController(val service: UserService) : Controller<UserWrapper>{

    @GetMapping
    fun findById(
                 response: HttpServletResponse, request: HttpServletRequest): UserWrapper {
        return service.findById(1)
        //TODO haal het id of wat we ook gebruiken voor authenticatie uit cookie en zoek het in de service.
        // Ofwel gwn de cookie zelf aan de service geven als parameter?
    }

    @PutMapping
    fun update(@RequestBody resource: UserWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.update(1, resource)
        //TODO haal het id of wat we ook gebruiken voor authenticatie uit cookie en zoek het in de service.
        // Ofwel gwn de cookie zelf aan de service geven als parameter?
    }

    @DeleteMapping
    fun delete(
               response: HttpServletResponse, request: HttpServletRequest) {
        if (service.deleteById(1) != 1){
            throw IllegalArgumentException("ID was not found so User could not be deleted")
        }
        //TODO haal het id of wat we ook gebruiken voor authenticatie uit cookie en zoek het in de service.
        // Ofwel gwn de cookie zelf aan de service geven als parameter?
    }

}