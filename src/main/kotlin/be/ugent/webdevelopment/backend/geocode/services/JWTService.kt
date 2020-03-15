package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
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

    private val headerString: String = "Token"

    fun tryAuthenticateGetUser(request: HttpServletRequest) : User {
        val userid: Int = this.tryAuthenticateGetId(request)
        val user: Optional<User> = userRepository.findById(userid)
        return user.orElseThrow { GenericException("Could not authenticate") }
    }

    //TODO: Use Cookies
    fun tryAuthenticateGetId(request: HttpServletRequest) : Int {
        val token: String = request.getHeader(headerString)
        val userid: Int = JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token)
                .subject.toInt()
        return userid
    }

    //TODO: Use Cookies
    fun addToken(user: User, response: HttpServletResponse) {
        val token: String = JWT.create()
                .withSubject(user.id.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + secretDuration.toLong()))
                .sign(Algorithm.HMAC512(secret))
        response.addHeader(headerString, token)
    }
}