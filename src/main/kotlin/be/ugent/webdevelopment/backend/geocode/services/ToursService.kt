package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.TourWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.TourRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ToursService {

    @Autowired
    lateinit var tourRepository: TourRepository

    @Autowired
    lateinit var locationsRepository: LocationRepository

    fun getTours(): List<Tour> {
        return tourRepository.getAllByActiveTrueAndListedTrue()
    }

    fun checkStringField(field: String, container: ExceptionContainer, fieldName: String, error: String, exceptionCondition: (String) -> Boolean) {
        if (exceptionCondition(field)) {
            container.addException(PropertyException(fieldName, error))
        }
    }

    fun checkName(field: String, container: ExceptionContainer) {
        checkStringField(field, container, "name", "The name should be between 3 and 255 characters long.") { it.length < 3 || it.length > 255 }
    }

    fun checkDescription(field: String, container: ExceptionContainer) {
        checkStringField(field, container, "description", "The name should be between 3 and 255 characters long.") { it.length < 5 || it.length > 2048 }
    }

    fun createTour(resource: TourWrapper, user: User): UUID {
        val container = ExceptionContainer()
        if (resource.name.isEmpty) {
            container.addException(PropertyException("name", "The name is an expected value."))
        } else {
            checkName(resource.name.get(), container)
        }
        if (resource.description.isEmpty) {
            container.addException(PropertyException("description", "The description is an expected value."))
        } else {
            checkDescription(resource.description.get(), container)
        }

        val locations: MutableList<Location> = Collections.emptyList()

        if (resource.locations.isEmpty) {
            container.addException(PropertyException("locations", "The locations are an expected value."))
        } else {
            resource.locations.get().forEach {
                val loc = locationsRepository.findBySecretId(it)
                if (loc.isEmpty) {
                    container.addException(PropertyException("locations", "The location with secretId = $it was not found in the database."))
                } else {
                    locations.add(loc.get())
                }
            }
        }
        if (resource.active.isEmpty) {
            container.addException(PropertyException("active", "Active is an expected value."))
        }
        if (resource.listed.isEmpty) {
            container.addException(PropertyException("listed", "Listed is an expected value."))
        }
        container.throwIfNotEmpty()
        val tour = Tour(
                creator = user,
                locations = resource.locations.get().map { locationsRepository.findBySecretId(it).get() },
                secretId = UUID.randomUUID().toString(),
                name = resource.name.get(),
                description = resource.description.get(),
                createdAt = Date.from(Instant.now()),
                listed = resource.listed.get(),
                active = resource.active.get()
        )
        return UUID.fromString(tourRepository.saveAndFlush(tour).secretId)
    }

    fun getTourById(secretId: UUID): Tour {
        return tourRepository.getBySecretId(secretId.toString()).orElseThrow {
            throw GenericException("The Tour with secretId = $secretId was not found in the database.")
        }
    }

    fun updateTour(secretId: UUID, resource: TourWrapper) {
        val tour = tourRepository.getBySecretId(secretId.toString()).orElseThrow {
            throw GenericException("Secret id is not linked to any tour.")
        }

        val container = ExceptionContainer()
        resource.name.ifPresent { checkName(it, container) }
        resource.description.ifPresent { checkDescription(it, container) }

        container.throwIfNotEmpty()
        resource.name.ifPresent { tour.name = it }//TODO misschien checks uitvoeren op lengte?
        resource.description.ifPresent { tour.description = it }//TODO misschien checks uitvoeren op lengte?
        resource.active.ifPresent { tour.active = it }
        resource.listed.ifPresent { tour.listed = it }
        tourRepository.saveAndFlush(tour)
    }

    fun deleteTour(secretId: UUID, user: User) {
        val tour = tourRepository.getBySecretId(secretId.toString()).orElseThrow {
            throw GenericException("Secret id is not linked to any tour.")
        }
        if (tour.creator.id != user.id && !(user.admin)) {
            throw GenericException("You did not create this tour and are not an andmin so you can not delete it.")
        } else {
            tourRepository.delete(tour)
        }
    }

    fun getByUser(user: User): List<Tour> {
        return tourRepository.getAllByCreator(user)
    }

}
