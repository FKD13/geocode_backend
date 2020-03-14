package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationsService {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    fun findAll(): List<LocationsWrapper> {
        return locationRepository.findAllByListedEquals(true).map { LocationsWrapper(it) }
    }

    fun findById(secret_id: UUID): LocationsWrapper {
        return LocationsWrapper(locationRepository.findBySecretId(secret_id.toString()).get())
        //TODO .get() naar safe expression omvormen
    }

}