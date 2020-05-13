package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.services.MainService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@JsonView(View.PublicDetail::class)
class MainController(
        val mainService: MainService
) {

    @Value("\${CAPTCHA_SITE}")
    private final lateinit var captchaSite: String

    @GetMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun mainJson(): ModelAndView {
        return ModelAndView("forward:/v3/api-docs")
    }

    @GetMapping("/", produces = [MediaType.TEXT_HTML_VALUE])
    fun mainHtml(): ModelAndView {
        return ModelAndView("redirect:/swagger/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config/")
    }

    @GetMapping("/privacyagreement")
    fun privacy(): String {
        return mainService.getPrivacyAgreement()
    }

    @GetMapping("/captchasite")
    fun getCaptcha(): String {
        return captchaSite
    }
}