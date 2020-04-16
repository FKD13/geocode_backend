package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/user")
@JsonView(View.PrivateDetail::class)
class UserController(val usersService: UsersService, val jwtService: JWTAuthenticator, val locationsService: LocationsService) {

    @GetMapping
    @JsonView(View.PrivateDetail::class)
    fun findByLoggedIn(
            response: HttpServletResponse, request: HttpServletRequest): User {
        return jwtService.tryAuthenticate(request)
    }

    @GetMapping(value = ["/locations"])
    @JsonView(View.PublicDetail::class) //TODO: change to View.List
    fun getLocations(response: HttpServletResponse, request: HttpServletRequest): List<LocationWrapper> {
        return locationsService.findAllByUser(jwtService.tryAuthenticate(request))
    }

    @PatchMapping
    fun update(@RequestBody resource: UserWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        usersService.update(jwtService.tryAuthenticate(request), resource)
    }

    @DeleteMapping
    fun delete(response: HttpServletResponse, request: HttpServletRequest) {
        usersService.deleteUser(jwtService.tryAuthenticate(request))
    }

    //------------------------------------------------------------------------------------------------------------------
    // Visits

    @GetMapping("/visits")
    fun getVisitsForUser(response: HttpServletResponse, request: HttpServletRequest) {
        //TODO
    }

    @GetMapping("/visits/{secretId}")
    fun getVisitsForUserByLocationSecret(@PathVariable secretId: UUID,
                                         response: HttpServletResponse, request: HttpServletRequest) {
        //TODO
    }
}