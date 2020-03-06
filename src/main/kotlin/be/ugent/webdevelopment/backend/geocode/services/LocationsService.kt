package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationsService {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    fun findAll(): List<Location> {
        return locationRepository.findAll()
    }

    fun findById(secret_id: UUID): Location {
        return locationRepository.findBySecretId(secret_id.toString())
    }

    fun create(resource: Location): UUID {
        return UUID.fromString(locationRepository.save(resource).secretId)
    }

    fun update(secret_id: UUID, resource: Location): Int {
        throw NotImplementedError("This has not been implemented")
    }

    fun deleteById(secret_id: UUID): Int{
        locationRepository.deleteBySecretId(secret_id.toString())
        return 1
    }

}