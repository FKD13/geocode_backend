package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository


    fun findById(id: Int): UserWrapper {
        val user : Optional<User> = userRepository.findById(id)
        if(user.isEmpty) throw Exception() //todo maak dit http exception
        return UserWrapper(user.get())
    }

    fun create(resource: UserWrapper): Int {
        throw NotImplementedError() // Een user mag enkel aangemaakt worden via Oauth
    }

    fun update(id: Int, resource: UserWrapper): Int {
        throw NotImplementedError("This has not been implemented")
        //return userRepository.updateUser(id, resource)
    }

    fun deleteById(id: Int): Int {
        userRepository.deleteById(id)
        return 1
    }

}
