package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.dao.UserDao
import org.springframework.stereotype.Service

import be.ugent.webdevelopment.backend.geocode.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

@Service
class UsersService: ServiceInterface<User, Long>{

    @Autowired
    @Qualifier("fakeUserDao")
    private lateinit var userDao: UserDao

    override fun findAll(): List<User> {
        return userDao.getAllUsers()
    }

    override fun findById(id: Long): User {
        return userDao.getUserById(id)!! //TODO dit moet mooier worden en een http error gooien
    }

    override fun create(resource: User): Long {
        return userDao.insertUser(resource)
    }

    override fun update(id: Long, resource: User): Int {
        return userDao.updateUser(id, resource)
    }

    override fun deleteById(id: Long): Int {
        return userDao.deleteUser(id)
    }

}