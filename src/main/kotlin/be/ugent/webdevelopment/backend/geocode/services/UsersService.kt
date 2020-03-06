package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class UsersService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun findAll(): List<UsersWrapper> {
        return userRepository.findAll().map { user -> UsersWrapper(user) }
    }

    fun findById(id: Int): UsersWrapper {
        return UsersWrapper(userRepository.findById(id).get())
        //TODO dit moet mooier worden en een http error gooien als findbyid niet lukt
    }

    fun create(resource: UsersWrapper): Int {
        throw NotImplementedError() //TODO dit mag enkel opgeroepen worden met de Oauth
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