package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserLoginWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.services.AuthService
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/auth")
class AuthController(val service: AuthService, val jwtService: JWTAuthenticator, val usersService: UsersService) {

    @PostMapping(value = ["/login"])
    fun login(@RequestBody resource: UserLoginWrapper, response: HttpServletResponse) {
        service.tryLogin(resource)
        jwtService.addToken(usersService.findByEmail(resource.email), response)
    }

    @PostMapping(value = ["/logout"])
    fun logout() : String {
        return "Hello"
    }

    @PostMapping(value = ["/register"])
    fun register(@RequestBody resource: UserRegisterWrapper) {
        service.tryRegister(resource)
    }

    @GetMapping(value = ["/taken/{username}"])
    fun checkUser(@PathVariable username: String) : Boolean {
        return service.checkUser(username)
    }
}

