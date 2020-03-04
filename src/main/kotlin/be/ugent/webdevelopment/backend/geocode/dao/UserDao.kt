package be.ugent.webdevelopment.backend.geocode.dao

import be.ugent.webdevelopment.backend.geocode.model.User
import kotlin.random.Random

interface UserDao {

    fun insertUser(id: Long, user: User) : Long
    fun insertUser(user: User): Long{
        val id: Long = Random.nextLong()
        return insertUser(id, user)
    }

    fun getAllUsers(): List<User>

    fun getUserById(id: Long): User?

    fun updateUser(id: Long, user: User): Int

    fun deleteUser(id: Long): Int



}