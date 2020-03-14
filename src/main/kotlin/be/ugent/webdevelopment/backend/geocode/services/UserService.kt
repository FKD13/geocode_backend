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

    fun create(resource: UserWrapper){
        throw NotImplementedError() // Een user mag enkel aangemaakt worden via auth
    }

    fun update(id: Int, resource: UserWrapper){
        
    }

    fun deleteById(id: Int) {
        userRepository.deleteById(id)

    }

}
