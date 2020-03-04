package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.model.User
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException


@RestController
@RequestMapping("/user")
class UsersController(val service: UsersService): Controller<User, Long> {

    override fun findAll(): List<User> {
        return this.service.findAll()
    }

    override fun findById(id: Long): User {
        return this.service.findById(id)
    }

    override fun create(resource: User): Long {
        return this.service.create(resource)
    }

    override fun update(id: Long, resource: User) {
        this.service.update(id, resource)
    }

    override fun delete(id: Long) {
        if (this.service.deleteById(id) != 1){
            throw IllegalArgumentException("ID was not found so User could not be deleted")
        }
    }

}