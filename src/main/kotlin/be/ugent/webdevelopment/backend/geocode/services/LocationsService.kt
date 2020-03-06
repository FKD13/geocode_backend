package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Location
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
        return locationRepository.findAll().map { location -> LocationsWrapper(location) }
    }

    fun findById(secret_id: UUID): LocationsWrapper {
        return LocationsWrapper(locationRepository.findBySecretId(secret_id.toString()))
    }

    fun create(resource: LocationsWrapper): UUID {
        return UUID.fromString(locationRepository.saveAndFlush(
                //TODO al deze dubbele uitroeptekens veranderen.
                // Checks uitvoeren en daarmee de juiste error terug geven
                Location(longitude = resource.longitude!!,
                        latitude = resource.latitude!!,
                        secretId = UUID.randomUUID().toString(),
                        description = resource.description!!,
                        creator = userRepository.findById(resource.creatorId!!).get())
        ).secretId)
    }

    fun update(secret_id: UUID, resource: LocationsWrapper){
        val location = locationRepository.findBySecretId(secret_id = secret_id.toString())
        // hier mogen de '!!' wel blijven staan, de check staat er telkens boven
        if (resource.creatorId != null){
            location.creator = userRepository.findById(resource.creatorId!!).get()
        }
        if (resource.longitude != null){
            location.longitude = resource.longitude!!
        }
        if (resource.latitude != null){
            location.latitude = resource.latitude!!
        }
        if (resource.name != null){
            location.name = resource.name!!
        }
        if (resource.description != null){
            location.description = resource.description!!
        }
        locationRepository.saveAndFlush(location)
    }

    fun deleteById(secret_id: UUID): Int{
        locationRepository.deleteBySecretId(secret_id.toString())
        return 1
    }

}