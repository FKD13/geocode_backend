package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.TourWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ToursStatistics
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
import kotlin.collections.ArrayList
import kotlin.math.*

@Service
class ToursService {

    @Autowired
    lateinit var tourRepository: TourRepository

    @Autowired
    lateinit var locationsRepository: LocationRepository

    @Autowired
    lateinit var achievementService: AchievementService

    fun getTours(): List<Tour> {
        return tourRepository.getAllByActiveTrueAndListedTrue()
    }

    fun checkStringField(field: String, container: ExceptionContainer, fieldName: String, error: String, exceptionCondition: (String) -> Boolean) {
        if (exceptionCondition(field)) {
            container.addException(PropertyException(fieldName, error))
        }
    }

    fun checkName(field: String, container: ExceptionContainer) {
        checkStringField(field, container, "name", "The name should be between 3 and 64 characters long.")
        { it.length < 3 || it.length > 64 }
    }

    fun checkDescription(field: String, container: ExceptionContainer) {
        checkStringField(field, container, "description", "The name should be between 3 and 2048 characters long.")
        { it.length < 5 || it.length > 2048 }
    }

    fun createTour(resource: TourWrapper, user: User): Tour {
        val container = ExceptionContainer()

        val date = Calendar.getInstance().apply {
            add(Calendar.HOUR, -1)
        }

        if (!user.admin) {
            if (tourRepository.findAllByCreatorAndCreatedAtIsAfter(user, date = date.time).isNotEmpty()) {
                throw GenericException("You can only create a tour every hour.")
            }
        }

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

        val locations: MutableList<Location> = ArrayList()

        if (resource.locations.isEmpty) {
            container.addException(PropertyException("locations", "The locations are an expected value."))
        } else {
            checkLocations(resource.locations.get(), container)
            resource.locations.get().forEach { locations.add(locationsRepository.findBySecretId(it).get()) }
        }
        if (resource.active.isEmpty) {
            container.addException(PropertyException("active", "Active is an expected value."))
        }
        if (resource.listed.isEmpty) {
            container.addException(PropertyException("listed", "Listed is an expected value."))
        }
        container.let {
            it.ifNotEmpty {
                it.addException(GenericException("The tour could not be created."))
                throw it
            }
        }
        val tour = Tour(
                creator = user,
                locations = locations,
                secretId = UUID.randomUUID().toString(),
                name = resource.name.get(),
                description = resource.description.get(),
                createdAt = Date.from(Instant.now()),
                listed = resource.listed.get(),
                active = resource.active.get(),
                totalDistance = calcTotalDist(locations)
        )
        val tourRes = tourRepository.saveAndFlush(tour)
        achievementService.validateAchievementsAsync(user)
        return tourRes
    }

    private fun calcTotalDist(list: List<Location>): Double {
        var res = 0.0
        val R = 6371.230
        for (i in 1 until list.size) {
            val prevLoc = list[i - 1]
            val loc = list[i]
            val latDiff = (loc.latitude - prevLoc.latitude) * (PI / 180.0)
            val lonDiff = (loc.longitude - prevLoc.longitude) * (PI / 180.0)
            val a = sin(latDiff / 2.0).pow(2) + (cos(loc.latitude * (PI / 180.0)) *
                    cos(prevLoc.latitude * (PI / 180.0)) * sin(lonDiff / 2.0).pow(2))
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            res += (R * c)
        }
        return res
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

        resource.name.ifPresent { tour.name = it }
        resource.description.ifPresent { tour.description = it }
        resource.active.ifPresent { tour.active = it }
        resource.listed.ifPresent { tour.listed = it }
        tourRepository.saveAndFlush(tour)
    }

    private fun checkLocations(list: List<String>, container: ExceptionContainer) {
        list.forEach {
            if (locationsRepository.findBySecretId(it).isEmpty) {
                container.addException(PropertyException("locations", "The location with secretId = $it was not found in the database."))
            }
        }
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

    fun statistics(secretId: UUID): ToursStatistics {
        val tour = getTourById(secretId)
        return ToursStatistics(
                completionCount = tour.user_tours.filter { it.completed }.count()
        )
    }
}
