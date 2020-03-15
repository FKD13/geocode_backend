package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.awt.LinearGradientPaint
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.ZoneId
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class JWTAuthenticator {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Value("\${GEOCODE_SECRET}")
    private lateinit var secret: String

    @Value("\${GEOCODE_SECRET_DURATION}")
    private lateinit var secretDuration: String

    private val cookieToken: String = "Geocode_Token"

    fun tryAuthenticate(request: HttpServletRequest) : User {
        val user: Optional<User>
        var token: Optional<String> = Optional.empty()
        println(request.cookies)
        if (request.cookies == null) {
            throw GenericException(code = HttpStatus.UNAUTHORIZED, message = "Not logged in")
        }
        request.cookies.map {if(it.name.equals(cookieToken)) {token = Optional.of(it.value)}}
        token.orElseThrow { GenericException(code = HttpStatus.UNAUTHORIZED, message = "Not logged in") }
        val jwtToken: DecodedJWT = JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token.get())
        if (jwtToken.expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(now())) {
            throw GenericException(code = HttpStatus.UNAUTHORIZED, message = "Not logged in")
        } else {
            try {
                user = userRepository.findById(jwtToken.subject.toInt())
                if (user.isEmpty) {
                    throw GenericException(code = HttpStatus.UNAUTHORIZED, message = "Not logged in")
                }
                return user.get()
            } catch (e: NumberFormatException) {
                throw GenericException(code = HttpStatus.UNAUTHORIZED, message = "Not logged in")
            }
        }
    }

    fun addToken(user: User, response: HttpServletResponse) {
        response.addCookie(Cookie(cookieToken, JWT.create()
                .withSubject(user.id.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + secretDuration.toLong()))
                .sign(Algorithm.HMAC512(secret))).also {it.isHttpOnly = true}.also {it.path = "/"}) //TODO: be more specific than "/"
        //TODO: add secure
    }
}