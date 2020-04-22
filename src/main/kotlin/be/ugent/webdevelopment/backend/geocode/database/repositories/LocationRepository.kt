package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LocationRepository : JpaRepository<Location, Int> {
    fun findAllByListedAndActive(listed: Boolean, active: Boolean): List<Location>
    fun findByCreator(creator: User): List<Location>
    fun findBySecretId(secretId: String): Optional<Location>
    fun findByVisitSecret(visitSecret: String): Optional<Location>
    fun findByVisitSecretAndActive(visitSecret: String, active: Boolean): Optional<Location>
}