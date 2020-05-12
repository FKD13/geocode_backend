package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LocationRatingRepository : JpaRepository<LocationRating, Int> {
    fun findAllByLocation(location: Location): List<LocationRating>
    fun findByCreatorAndLocation(creator: User, location: Location): Optional<LocationRating>
    fun findAllByCreator(creator: User): List<LocationRating>
}