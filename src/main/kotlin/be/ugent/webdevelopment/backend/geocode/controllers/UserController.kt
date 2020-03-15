package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import be.ugent.webdevelopment.backend.geocode.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/user")
class UserController(val service: UserService, val jwtService: JWTAuthenticator, val locationsService: LocationsService) : Controller<UserWrapper>{

    @GetMapping
    fun findByLoggedIn(
                 response: HttpServletResponse, request: HttpServletRequest): UserWrapper {
        return UserWrapper(jwtService.tryAuthenticate(request))
    }

    @GetMapping(value = ["/locations"])
    fun getLocations(response: HttpServletResponse, request: HttpServletRequest): List<LocationWrapper>{
        return locationsService.findAllByUser(jwtService.tryAuthenticate(request))
    }

    @PatchMapping
    fun update(@RequestBody resource: UserWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.update(jwtService.tryAuthenticate(request), resource)
    }

    @DeleteMapping
    fun delete(
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteUser(jwtService.tryAuthenticate(request))
    }

}