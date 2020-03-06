package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class UsersService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun findById(id: Int): User {
        return userRepository.findById(id).get() //TODO dit moet mooier worden en een http error gooien
    }

    fun create(resource: User): Int {
        return userRepository.save(resource).id
    }

    fun update(id: Int, resource: User): Int {
        throw NotImplementedError("This has not been implemented")
        //return userRepository.updateUser(id, resource)
    }

    fun deleteById(id: Int): Int {
        userRepository.deleteById(id)
        return 1
    }

}