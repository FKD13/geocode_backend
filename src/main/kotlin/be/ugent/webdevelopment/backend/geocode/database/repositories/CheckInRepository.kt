package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CheckInRepository : JpaRepository<CheckIn, Int> {
    fun findAllByCreator(user: User): List<CheckIn>
    fun findAllByLocation(location: Location): List<CheckIn>
    fun findAllByCreatorAndLocationAndCreatedAtAfter(user: User, location: Location, date: Date): List<CheckIn>
    fun findAllByLocationAndCreator(location: Location, user: User): List<CheckIn>
    fun findAllByLocationOrderByCreatedAt(location: Location): List<CheckIn>
    fun findFirstByLocationOrderByCreatedAtDesc(location: Location): CheckIn?
}
   