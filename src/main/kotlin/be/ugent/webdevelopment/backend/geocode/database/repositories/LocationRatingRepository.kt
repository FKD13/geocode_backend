package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRatingRepository : JpaRepository<LocationRating, Int> {

    fun findAllByLocation(location: Location): List<LocationRating>

}