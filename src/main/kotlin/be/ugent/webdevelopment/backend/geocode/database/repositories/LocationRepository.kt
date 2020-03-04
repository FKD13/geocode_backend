package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import org.springframework.data.repository.CrudRepository

interface LocationRepository : CrudRepository<Location, Long>