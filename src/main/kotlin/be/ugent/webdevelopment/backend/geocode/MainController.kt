package be.ugent.webdevelopment.backend.geocode

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/")
    fun main() : String {
        return "Hello There!"
    }
}