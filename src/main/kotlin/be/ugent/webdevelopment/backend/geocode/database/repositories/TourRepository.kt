package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TourRepository : JpaRepository<Tour, Int> {
    fun getAllByActiveTrueAndListedTrue(): List<Tour>
    fun getBySecretId(secretId: String): Optional<Tour>
    fun getAllByCreator(creator: User): List<Tour>
}