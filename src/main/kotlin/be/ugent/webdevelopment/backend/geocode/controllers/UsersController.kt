package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UsersWrapper
import be.ugent.webdevelopment.backend.geocode.services.UsersService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException


@RestController
@RequestMapping("/users")
class UsersController(val service: UsersService): Controller<UsersWrapper, Int> {

    override fun findAll(): List<UsersWrapper> {
        return this.service.findAll()
    }

    override fun findById(id: Int): UsersWrapper {
        return this.service.findById(id)
    }

    override fun create(resource: UsersWrapper): Int {
        return this.service.create(resource)
    }

    override fun update(id: Int, resource: UsersWrapper) {
        this.service.update(id, resource)
    }

    override fun delete(id: Int) {
        if (this.service.deleteById(id) != 1){
            throw IllegalArgumentException("ID was not found so User could not be deleted")
        }
    }

}