package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import java.net.MalformedURLException
import java.net.URL
import java.util.*

@Service
class UsersService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authService: AuthService

    fun findByEmail(email: String): User{
        if(userRepository.findByEmail(email).isPresent) return userRepository.findByEmail(email).get()
        throw GenericException("User with email = $email was not found in the database")
    }

    fun findAll(): List<UsersWrapper> {
        return userRepository.findAll().map { user -> UsersWrapper(user) }
    }

    fun findById(id: Int): UsersWrapper {
        val user : Optional<User> = userRepository.findById(id)
        if(user.isEmpty) throw GenericException("User with id = $id was not found in the database")
        return UsersWrapper(user.get())
    }


    fun checkUrl(url: String, container: ExceptionContainer){
        try {
            URL(url)
        }catch (e : MalformedURLException){
            container.addException(PropertyException("url", "The url is not valid."))
        }
    }

    fun checkUsername(nameResource: String, nameUser: String, container: ExceptionContainer){
        if(nameResource.length < 3) {
            container.addException(PropertyException("username", "Should be longer than 3 characters"))
        }
        if(nameResource.length > 30) {
            container.addException(PropertyException("username", "Should be shorter than 30 characters"))
        }
        userRepository.findByUsernameIgnoreCase(nameResource).ifPresent {
            if (it.username != nameUser){
                container.addException(PropertyException("username", "This username is already in use."))
            }
        }
    }

    fun update(user: User, resource: UserWrapper){
        val container = ExceptionContainer()

        resource.email.ifPresent {
            if(authService.checkEmail(resource.email.get()).not())
                container.addException(PropertyException("email", "The email is not valid"))
            checkEmailUnique(user.email, resource.email.get(), container)
        }
        resource.avatarUrl.ifPresent { checkUrl(resource.avatarUrl.get(), container) }
        resource.username.ifPresent { checkUsername(resource.username.get(), user.username, container) }

        container.throwIfNotEmpty()

        resource.email.ifPresent { user.email = resource.email.get() }
        resource.avatarUrl.ifPresent { user.avatarUrl = resource.avatarUrl.get() }
        resource.username.ifPresent { user.username = resource.username.get() }
        userRepository.saveAndFlush(user)

    }

    private fun checkEmailUnique(emailUser: String, emailResource: String, container: ExceptionContainer) {
        userRepository.findByEmail(emailResource).ifPresent {
            if (it.email != emailUser){
                container.addException(PropertyException("email", "Email already exists, try again." ))
            }
        }
    }

    fun deleteUser(user: User) {
        userRepository.delete(user)
        userRepository.flush()
    }



}