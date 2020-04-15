package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CheckInRepository : JpaRepository<CheckIn, Int> {
    fun findAllByLocationOrderByTime(location: Location): List<CheckIn>
    fun findAllByCreator(user: User): List<CheckIn>
    fun findAllByLocationAndCreator(location: Location, user: User): List<CheckIn>
    fun findAllByCreatorAndLocation(creator: User, location: Location): Optional<CheckIn>
}
   