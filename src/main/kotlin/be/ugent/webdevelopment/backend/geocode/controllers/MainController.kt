package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.View
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@JsonView(View.PublicDetail::class)
class MainController {

    @Value("\${CAPTCHA_SITE}")
    private final lateinit var captchaSite: String

    @GetMapping("/")
    fun main(): String {
        return "Hello There!"
    }


    @GetMapping("/captchasite")
    fun getCaptcha(): String {
        return captchaSite
    }
}