package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
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
        if(user.isEmpty) throw GenericException("User with id = $id was not found in the database")
        return UsersWrapper(user.get())
    }

}