package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/user")
class UserController(val jwtService: JWTAuthenticator, val locationsService: LocationsService) : Controller<UserWrapper>{

    @GetMapping
    fun findByLoggedIn(
                 response: HttpServletResponse, request: HttpServletRequest): UserWrapper {
        return UserWrapper(jwtService.tryAuthenticate(request))
    }

    @GetMapping(value = ["/locations"])
    fun getLocations(response: HttpServletResponse, request: HttpServletRequest): List<LocationWrapper>{
        return locationsService.findAllByUser(jwtService.tryAuthenticate(request))
    }


}