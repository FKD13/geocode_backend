package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.net.MalformedURLException
import java.net.URL

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authService: AuthService

    /* This function will probably not be needed, this is done in the JWT Controller r*/
    fun findById(id: Int): UserWrapper {
        val user : Optional<User> = userRepository.findById(id)
        if(user.isEmpty) throw GenericException("The user with id= $id was not found in the database.")
        return UserWrapper(user.get())
    }

    fun checkUrl(url: String, container: ExceptionContainer){
        try {
            val test : URL = URL(url)
        }catch (e : MalformedURLException){
            container.addException(PropertyException("url", "The url is not valid."))
        }
    }

    fun checkUsername(name: String, container: ExceptionContainer){
        if(name.length < 3) {
            container.addException(PropertyException("username", "Should be longer than 3 characters"))
        }
        if(name.length > 30) {
            container.addException(PropertyException("username", "Should be shorter than 30 characters"))
        }
        if (userRepository.findByUsernameIgnoreCase(name).isEmpty.not()){
            container.addException(PropertyException("username", "This username is already in use."))
        }
    }

    fun update(id: Int, resource: UserWrapper){
        val container : ExceptionContainer = ExceptionContainer()

        resource.email.ifPresent { if(authService.checkEmail(resource.email.get()).not()) container.addException(PropertyException("email", "The email is not valid"))  }
        resource.avatarUrl.ifPresent { checkUrl(resource.avatarUrl.get(), container) }
        resource.username.ifPresent { checkUsername(resource.username.get(), container) }

        container.throwIfNotEmpty()

        userRepository.findById(id).ifPresentOrElse({
            val user: User = it
            resource.email.ifPresent { user.email = resource.email.get() }
            resource.avatarUrl.ifPresent { user.avatarUrl = resource.avatarUrl.get() }
            resource.username.ifPresent { user.username = resource.username.get() }
            userRepository.saveAndFlush(user)
        }, {
            throw GenericException("The user with id = $id was not found in the database")
        })




    }

    fun deleteById(id: Int) {
        userRepository.findById(id).ifPresentOrElse(
                {
                    userRepository.deleteById(id)
                }, {
                    throw GenericException("The User with id= $id could not be found and could therefor also not be deleted.")
                })
    }

}
