package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
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
    private val usernamePattern = Pattern.compile("[A-Za-z0-9 \\-_]+")

    private val mail = InternetAddress()

    fun checkUser(username: String) : Boolean {
        val user : Optional<User> = userRepository.findByUsername(username);
        return !user.isEmpty
    }

    fun tryRegister(resource: UserRegisterWrapper) {
        if(resource.username.length < 3) {
            throw PropertyException("username", "Username can't be less than 3 characters")
        }

        if(resource.username.length > 30) {
            throw PropertyException("username", "Username can't be longer than 30 characters")
        }

        if(usernamePattern.matcher(resource.username).matches())
            throw PropertyException("username", "Password can only contain letters, numbers, space - and _")
        
        if(resource.email.length < 5) {
            throw PropertyException("email", "Username can't be less than 5 characters")
        }

        try {
            InternetAddress(resource.email).validate()
        } catch (e: AddressException) {
            throw PropertyException("email", "Invalid email adress")
        }

        if(resource.password != resource.passwordRepeat) {
            throw PropertyException("passwordRepeat", "Passwords didn't match, try again.", HttpStatus.BAD_REQUEST)
        }

        if(passwordPattern.matcher(resource.password).matches())
            throw PropertyException("password", "Password should not contain ` ´ ' and \"")

        if(resource.captcha.isEmpty) {
            throw PropertyException("captcha", "Empty captcha, try again.", HttpStatus.BAD_REQUEST)
        }

        captchaService.validateCaptcha(resource.captcha.get())

        val existingUser = userRepository.findByEmailOrUsername(resource.email, resource.username)
        if(existingUser.isPresent) {
            val user = existingUser.get()
            if(user.username.toLowerCase() == resource.username.toLowerCase()) {
                throw PropertyException("username", "Username already exists, try again.", HttpStatus.BAD_REQUEST)
            }

            if(user.email.toLowerCase() == resource.email.toLowerCase()) {
                throw PropertyException("email", "Email already exists, try again.", HttpStatus.BAD_REQUEST)
            }
        }




    }
}