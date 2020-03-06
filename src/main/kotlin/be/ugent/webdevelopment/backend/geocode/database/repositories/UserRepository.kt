package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): List<User>
}