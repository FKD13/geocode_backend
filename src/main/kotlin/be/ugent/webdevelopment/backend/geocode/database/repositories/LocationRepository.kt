package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LocationRepository : JpaRepository<Location, Int> {
    fun findByCreator(creator: User): List<Location>
    fun findBySecretId(secretId: String): Optional<Location>
    fun deleteBySecretId(secretId: String)
    fun findAllByListedAndActive(listed: Boolean, active: Boolean) : List<Location>
    fun findByVisitSecret(visitSecret: String) : Optional<Location>
}