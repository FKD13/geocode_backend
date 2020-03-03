package be.ugent.webdevelopment.backend.geocode.services

import org.springframework.stereotype.Service

import be.ugent.webdevelopment.backend.geocode.User

@Service
class UserService : ServiceInterface<User>{

    override fun findAll(): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(resource: User): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getById(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(resource: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteById(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}