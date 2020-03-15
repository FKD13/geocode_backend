package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserLoginWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.lang.reflect.Field
import java.util.*
import java.util.regex.Pattern
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.validation.ConstraintValidatorContext
import javax.validation.constraints.Email
import javax.xml.validation.Validator

@Service
class AuthService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var captchaService: CaptchaService

    private val passwordPattern = Pattern.compile("^.*['\"`´].*$")
    private val usernamePattern = Pattern.compile("^[^ ][A-Za-z0-9 \\-_]+[^ ]$")

    private val bCryptRounds = 12

    fun checkUser(username: String) : Boolean {
        val user : Optional<User> = userRepository.findByUsernameIgnoreCase(username)
        return !user.isEmpty
    }

    fun checkEmail(email: String) : Boolean {
        try {
            InternetAddress(email).validate()
        } catch (e: AddressException) {
            return false
        }
        return true
    }

    fun tryLogin(resource: UserLoginWrapper) {
        val user = userRepository.findByEmail(resource.email)

        if(user.isEmpty || BCrypt.checkpw(resource.password, user.get().password).not()) {
            val exc = ExceptionContainer(code = HttpStatus.BAD_REQUEST)
            exc.addException(GenericException("Unable to login."))
            exc.addException(PropertyException("email", "Email and/or password is wrong."))
            exc.addException(PropertyException("password", "Email and/or password is wrong."))
            throw exc
        }
    }

    fun tryRegister(resource: UserRegisterWrapper) {
        val exc = ExceptionContainer(code = HttpStatus.BAD_REQUEST)
        if(resource.username.length < 3) {
            exc.addException(PropertyException("username", "Should be at least 3 characters"))
        }

        if(resource.username.length > 30) {
            exc.addException(PropertyException("username", "Should be at most 30 characters"))
        }

        if(!usernamePattern.matcher(resource.username).matches()) {
            exc.addException(PropertyException("username", "You can use letters, numbers, spaces, underscores & hyphens, but it can't begin or end with a space."))
        }

        if(resource.email.length > 320) {
            exc.addException(PropertyException("email", "Should be at most 320 characters"))
        }

        if(!checkEmail(resource.email)) {
            exc.addException(PropertyException("email", "Invalid email adress"))
        }

        if(resource.password != resource.passwordRepeat) {
            exc.addException(PropertyException("passwordRepeat", "Passwords didn't match, try again."))
        }

        if(resource.password.length < 8) {
            exc.addException(PropertyException("password", "Should be at least 8 characters"))
        }

        if(resource.password.length > 64) {
            exc.addException(PropertyException("password", "Should be at most 64 characters"))
        }

        if(passwordPattern.matcher(resource.password).matches()) {
            exc.addException(PropertyException("password", "Should not contain ` ´ ' or \""))
        }

        if(resource.captcha.isEmpty) {
            exc.addException(GenericException("Empty captcha, try again."))
        } else {
            try {
                captchaService.validateCaptcha(resource.captcha.get())
            } catch (e: GenericException) {
                exc.addException(e)
            }
        }

        val existingUser = userRepository.findByEmailOrUsernameIgnoreCase(resource.email, resource.username)
        if(existingUser.isPresent) {
            val user = existingUser.get()
            if(user.username.toLowerCase() == resource.username.toLowerCase()) {
                exc.addException(PropertyException("username", "Username already exists, try again."))
            }

            if(user.email.toLowerCase() == resource.email.toLowerCase()) {
                exc.addException(PropertyException("email", "Email already exists, try again."))
            }
        }

        if (exc.isEmpty().not()){
            exc.addException(GenericException("Unable to log in."))
            exc.throwIfNotEmpty()
        }

        val hash = BCrypt.hashpw(resource.password, BCrypt.gensalt(bCryptRounds))

        userRepository.saveAndFlush(User(
                email = resource.email,
                username = resource.username,
                password = hash))

    }
}
