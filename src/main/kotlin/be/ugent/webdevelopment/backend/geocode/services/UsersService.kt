package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@Service
class UsersService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun findAll(): List<UsersWrapper> {
        return userRepository.findAll().map { user -> UsersWrapper(user) }
    }

    fun findById(id: Int): UsersWrapper {
        val user : Optional<User> = userRepository.findById(id)
        if(user.isEmpty) throw Exception()
        return UsersWrapper(user.get())
    }

    fun create(resource: UsersWrapper): Int {
        throw NotImplementedError() // Een user mag enkel aangemaakt worden via Oauth
    }

    fun update(id: Int, resource: UsersWrapper): Int {
        throw NotImplementedError("This has not been implemented")
        //return userRepository.updateUser(id, resource)
    }

    fun deleteById(id: Int): Int {
        userRepository.deleteById(id)
        return 1
    }

}