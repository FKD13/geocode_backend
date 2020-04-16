package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface CheckInRepository : JpaRepository<CheckIn, Int> {
    fun findAllByLocationOrderByCreatedAt(location: Location): List<CheckIn>
    fun findAllByCreator(user: User): List<CheckIn>
    fun findAllByLocationAndCreator(location: Location, user: User): List<CheckIn>
}
   