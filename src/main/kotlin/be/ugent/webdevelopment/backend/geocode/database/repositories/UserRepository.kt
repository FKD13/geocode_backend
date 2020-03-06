package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): List<User>
}