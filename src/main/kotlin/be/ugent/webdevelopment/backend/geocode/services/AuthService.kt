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

    private val emailPattern = Pattern.compile("[A-Za-z0-9_%+-]+@[A-Za-z0-9.-].[a-zA-Z]{2,4}")
    private val passwordPattern = Pattern.compile("^.*['\"`´].*$")
    private val usernamePattern = Pattern.compile("^[A-Za-z0-9 \\-_]+$")

    private val mail = InternetAddress()

    fun checkUser(username: String) : Boolean {
        val user : Optional<User> = userRepository.findByUsernameIgnoreCase(username)
        return !user.isEmpty
    }

    fun tryLogin(resource: UserLoginWrapper) {
        val user = userRepository.findByEmail(resource.email)

        //TODO: hash password to check!
        if(user.isEmpty || user.get().password != resource.password) {
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
            exc.addException(PropertyException("username", "Should be longer than 3 characters"))
        }

        if(resource.username.length > 30) {
            exc.addException(PropertyException("username", "Should be shorter than 30 characters"))
        }

        if(!usernamePattern.matcher(resource.username).matches()) {
            exc.addException(PropertyException("username", "You can use letters, numbers, spaces, underscores & hyphens."))
        }

        if(resource.email.length < 5) {
            exc.addException(PropertyException("email", "Should be longer than 5 characters"))
        }

        try {
            InternetAddress(resource.email).validate()
        } catch (e: AddressException) {
            exc.addException(PropertyException("email", "Invalid email adress"))
        }

        if(resource.password != resource.passwordRepeat) {
            exc.addException(PropertyException("passwordRepeat", "Passwords didn't match, try again."))
        }

        if(resource.password.length < 8) {
            exc.addException(PropertyException("password", "Should be longer than 8 characters"))
        }

        if(resource.password.length > 64) {
            exc.addException(PropertyException("password", "Should be shorter than 30 characters"))
        }

        if(passwordPattern.matcher(resource.password).matches()) {
            exc.addException(PropertyException("password", "Should not contain ` ´ ' or \""))
        }

        if(resource.captcha.isEmpty) {
            exc.addException(PropertyException("captcha", "Empty captcha, try again."))
        }

        //TODO: FIX CAPTCHA
        //captchaService.validateCaptcha(resource.captcha.get())

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

        if(!exc.isEmpty()) {
            exc.addException(GenericException("Unable to create account."))
            throw exc
        }

        //TODO: Hash & Salt password!!
        userRepository.saveAndFlush(User(
                email = resource.email,
                username = resource.username,
                password = resource.password))

    }
}
