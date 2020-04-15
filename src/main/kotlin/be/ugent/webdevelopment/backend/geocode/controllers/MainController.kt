package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.View
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@JsonView(View.PublicDetail::class)
class MainController {

    @GetMapping("/")
    fun main(): String {
        return "Hello There!"
    }
}