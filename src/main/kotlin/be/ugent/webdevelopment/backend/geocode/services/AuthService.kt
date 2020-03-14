package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

class AuthService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var captchaService: CaptchaService

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

        //TODO: check username allowed characters
        
        if(resource.email.length < 5) {
            throw PropertyException("email", "Username can't be less than 5 characters")
        }

        //TODO: check email allowed characters

        if(resource.password != resource.passwordRepeat) {
            throw PropertyException("passwordRepeat", "Passwords didn't match, try again.", HttpStatus.BAD_REQUEST)
        }

        //TODO: check password allowed characters

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