package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/user")
class UserController(val service: UserService, val jwtService: JWTAuthenticator) : Controller<UserWrapper>{

    @GetMapping
    fun findByLoggedIn(
                 response: HttpServletResponse, request: HttpServletRequest): UserWrapper {
        return UserWrapper(jwtService.tryAuthenticateGetUser(request))
    }

    @PutMapping
    fun update(@RequestBody resource: UserWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.update(jwtService.tryAuthenticateGetId(request), resource)
    }

    @DeleteMapping
    fun delete(
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteById(jwtService.tryAuthenticateGetId(request))
    }

}