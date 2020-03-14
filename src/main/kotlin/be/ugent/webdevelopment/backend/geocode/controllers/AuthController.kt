package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.services.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/auth")
class AuthController(val service: AuthService) {

    @GetMapping(value = ["/login"])
    fun login() : String {

        return "Hello"
    }

    @GetMapping(value = ["/logout"])
    fun logout() : String {
        return "Hello"
    }

    @GetMapping(value = ["/register"])
    fun register(@RequestBody resource: UserRegisterWrapper) {
        service.tryRegister(resource)
    }

    @GetMapping(value = ["/taken/{username}"])
    fun checkUser(@PathVariable username: String) : Boolean {
        return service.checkUser(username)
    }
}

