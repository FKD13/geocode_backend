package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationsService {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    fun findAll(): List<LocationsWrapper> {
        return locationRepository.findAllByListedEquals(true).map { LocationsWrapper(it) }
    }

    fun findById(secret_id: UUID): LocationsWrapper {
        val loc : Optional<Location> = locationRepository.findBySecretId(secret_id.toString())
        loc.ifPresentOrElse({}, {throw GenericException("The location that corresponds to this secret id was not found.")})
        return LocationsWrapper(loc.get())
    }

}