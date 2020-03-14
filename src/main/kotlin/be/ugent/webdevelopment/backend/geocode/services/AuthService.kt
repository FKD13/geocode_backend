package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserRegisterWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun checkUser(username: String) : Boolean {
        val user : Optional<User> = userRepository.findByUsername(username);
        return !user.isEmpty
    }

    fun tryRegister(resource: UserRegisterWrapper) {
        val resourceEmail = resource.email.toLowerCase()
        if(resource.password != resource.passwordRepeat) {
            throw PropertyException("passwordRepeat", "Passwords didn't match, try again.", 400)
        }

        if(resource.captcha.isEmpty) {
            throw PropertyException("captcha", "Empty captcha, try again.", 400)
        }

        // TODO: check captcha validity

        val existingUser = userRepository.findByEmailOrUsername(resourceEmail, resource.username)
        if(existingUser.isPresent) {
            val user = existingUser.get()
            if(user.username == resource.username) {
                throw PropertyException("username", "Username already exists, try again.", 400)
            }

            if(user.email == resourceEmail) {
                throw PropertyException("email", "Email already exists, try again.", 400)
            }
        }




    }
}