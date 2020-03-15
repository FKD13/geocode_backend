package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import java.net.URI
import java.util.regex.Pattern

class CaptchaResponse(
    val success: Boolean?,

    @JsonProperty("challenge_ts")
    val challengeTs: String?,
    val action: String?,
    val hostname: String?,

    @JsonProperty("error-codes")
    val errorCodes: List<String>?
)

@Service
class CaptchaService {

    @Value("\${CAPTCHA_SITE}")
    private final lateinit var captchaSite: String

    @Value("\${CAPTCHA_SECRET}")
    private final lateinit var captchaSecret: String

    private final val action: String = "register"

    private val captchaPattern = Pattern.compile("[A-Za-z0-9_-]+")

    fun checkRespone(response: String) : Boolean {
        return response.isNotEmpty() && captchaPattern.matcher(response).matches()
    }

    fun validateCaptcha(response: String) {
        if (!checkRespone(response)) {
            throw GenericException(code = HttpStatus.BAD_REQUEST, message = "Not a valid captha response")
        }
        val verifyUri: URI = URI.create(
                "https://www.google.com/recaptcha/api/siteverify?secret=${captchaSecret}&response=${response}")
        println(verifyUri.toString())
        val captchaResponse : CaptchaResponse = RestTemplate().postForObject(verifyUri, CaptchaResponse::class.java)
        if (captchaResponse.success == null || !captchaResponse.success ) {
            throw GenericException(code = HttpStatus.BAD_REQUEST, message = "Captha failed")
        } else if (captchaResponse.action == null || captchaResponse.action == action) {}
    }

}