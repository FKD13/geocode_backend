package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException


@RestController
@RequestMapping("/users")
class UsersController(val service: UsersService): Controller<User, Int> {

    override fun findAll(): List<User> {
        return this.service.findAll()
    }

    override fun findById(id: Int): User {
        return this.service.findById(id)
    }

    override fun create(resource: User): Int {
        return this.service.create(resource)
    }

    override fun update(id: Int, resource: User) {
        this.service.update(id, resource)
    }

    override fun delete(id: Int) {
        if (this.service.deleteById(id) != 1){
            throw IllegalArgumentException("ID was not found so User could not be deleted")
        }
    }

}