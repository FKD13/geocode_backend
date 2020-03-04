package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import org.springframework.data.repository.CrudRepository

interface LocationRatingRepository : CrudRepository<LocationRating, Long>