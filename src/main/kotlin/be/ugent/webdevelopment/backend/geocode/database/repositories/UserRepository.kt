package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): Optional<User>
    fun findByEmailOrUsernameIgnoreCase(email: String, username: String): Optional<User>
    fun findByUsernameIgnoreCase(username: String): Optional<User>
}
