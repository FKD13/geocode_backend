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
        return locationRepository.findAllByListedEquals(true).map { LocationsWrapper(it) }
    }

    fun findById(secret_id: UUID): LocationsWrapper {
        return LocationsWrapper(locationRepository.findBySecretId(secret_id.toString())!!)
        //TODO !! naar safe expression omvormen
    }

    fun create(resource: LocationsWrapper): UUID {
        val loc : Location = Location(
                longitude = resource.longitude!!,
                latitude =  resource.latitude!!,
                secretId = UUID.randomUUID().toString(),
                listed = resource.listed!!,
                name = resource.name!!,
                description = resource.description!!,
                creator = userRepository.findById(resource.creatorId!!).get()
        )
        //TODO als er een waarde niet is meegegeven een correcte error geven en als de user met creatorId niet gevonden is ook
        return UUID.fromString(locationRepository.saveAndFlush(loc).secretId)
    }

    fun update(secret_id: UUID, resource: LocationsWrapper){
        val location : Location = locationRepository.findBySecretId(secret_id = secret_id.toString())!!
        //TODO !! naar safe expression omvormen

        resource.longitude?.let { location.longitude = resource.longitude!! }
        resource.latitude?.let { location.latitude = resource.latitude!! }
        resource.name?.let { location.name = resource.name!! }
        resource.description?.let { location.description = resource.description!! }
        resource.creatorId?.let { location.creator = userRepository.findById(resource.creatorId!!).get() }

        locationRepository.saveAndFlush(location)
    }

    fun deleteById(secret_id: UUID){
        locationRepository.deleteBySecretId(secret_id.toString())
    }

}