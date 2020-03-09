package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location, Int> {
    fun findByCreator(creator: User) : List<Location>
    fun findBySecretId(secret_id: String) : Location?
    fun deleteBySecretId(secret_id: String)
    fun findAllByListedEquals(listed: Boolean) : List<Location>
}