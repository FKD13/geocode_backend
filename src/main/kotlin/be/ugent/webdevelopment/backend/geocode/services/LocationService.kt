package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationService(var locationRepository: LocationRepository, var userRepository: UserRepository) {

    fun findAll(id: Int): List<LocationWrapper> {
        return locationRepository.findByCreator(userRepository.getOne(id)).map { LocationWrapper(it) }
    }

    fun checkLat(lat: Double, container: ExceptionContainer) : Double{
        
    }

    fun create(resource: LocationWrapper): UUID {
        val container : ExceptionContainer = ExceptionContainer()

        //todo check longitude
        //todo check latitude
        //todo check listed
        //todo check name
        //todo check description
        //todo check creatorid

        container.throwIfNotEmpty()
        val loc : Location = Location(
                longitude = resource.longitude.get(),
                latitude =  resource.latitude.get(),
                secretId = UUID.randomUUID().toString(),
                listed = resource.listed.get(),
                name = resource.name.get(),
                description = resource.description.get(),
                creator = userRepository.findById(resource.creatorId.get()).get()
        )
        return UUID.fromString(locationRepository.saveAndFlush(loc).secretId)
    }

    fun update(secretId: UUID, resource: LocationWrapper) {
        //todo check all the values ook voor ik ze update
        locationRepository.findBySecretId(secret_id = secretId.toString()).ifPresentOrElse({
            val location : Location = it
            resource.longitude.ifPresent { location.longitude = resource.longitude.get() }
            resource.latitude.ifPresent { location.latitude = resource.latitude.get() }
            resource.name.ifPresent { location.name = resource.name.get() }
            resource.description.ifPresent { location.description = resource.description.get() }
            resource.creatorId.ifPresent { location.creator = userRepository.findById(resource.creatorId.get()).get() }
            locationRepository.saveAndFlush(location)
        }, {
            throw GenericException("Location with secretId=$secretId does not exist in the database.")
        })
    }

    fun deleteById(secretId: UUID) {
        locationRepository.deleteBySecretId(secretId.toString())
    }

}
