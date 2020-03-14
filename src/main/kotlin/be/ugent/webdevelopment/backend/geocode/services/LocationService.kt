package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationService(var locationRepository: LocationRepository, var userRepository: UserRepository) {

    fun findAll(id: Int): List<LocationWrapper> {
        return locationRepository.findByCreator(userRepository.getOne(id)).map { LocationWrapper(it) }
    }

    fun create(resource: LocationWrapper): UUID {
        throw NotImplementedError()
    }

    fun update(secretId: UUID, resource: LocationWrapper) {
        throw NotImplementedError()
    }

    fun deleteById(secretId: UUID) {
        locationRepository.deleteBySecretId(secretId.toString())
    }

}
