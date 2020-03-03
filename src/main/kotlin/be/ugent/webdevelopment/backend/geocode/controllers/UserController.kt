package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.User
import be.ugent.webdevelopment.backend.geocode.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This class is just for the principle. This still needs a service to communicate to
 */

@RestController
@RequestMapping("/user")
class UserController: Controller<User> {

    @Autowired
    private lateinit var service: UserService

    override fun findAll(): List<User> {
        return this.service.findAll()
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): User {
        return this.service.findById(id)
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(resource: User): Long {
        return this.service.create(resource)
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(id: Long, resource: User) {
        this.service.update(resource)
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Long) {
        this.service.deleteById(id)
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}