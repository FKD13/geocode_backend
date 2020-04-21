package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.GeneralStatistics
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationStatistics
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserStatistics
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRatingRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class StatisticsService(
        val locationRepository: LocationRepository,
        val userRepository: UserRepository,
        val checkInRepository: CheckInRepository,
        val ratingRepository: LocationRatingRepository
) {

    fun getUserStatistics(user: User): UserStatistics {
        val visitedLocationsCount = HashSet<String>()
        val visitedCountriesCount = HashSet<String>()

        val checkIns = checkInRepository.findAllByCreator(user)

        checkIns.forEach {
            visitedCountriesCount.add(it.location.country.toLowerCase())
            visitedLocationsCount.add(it.location.visitSecret)
        }

        return UserStatistics(visitedLocationsCount.size, visitedCountriesCount.size, checkIns.size)
    }

    fun getStatistics(): GeneralStatistics {
        val countriesCount = HashSet<String>()

        val locations = locationRepository.findAllByListedAndActive(listed = true, active = true)

        locations.forEach {
            countriesCount.add(it.country.toLowerCase())
        }

        return GeneralStatistics(
                locationsCount = locations.size,
                usersCount = userRepository.findAll().size,
                visitsCount = checkInRepository.findAll().size,
                countriesCount = countriesCount.size
        )
    }

    fun getLocationStatistics(location: Location): LocationStatistics {
        return LocationStatistics(
                ratingsCount = ratingRepository.findAllByLocation(location).size,
                visitsCount = checkInRepository.findAllByLocation(location).size,
                lastVisit = checkInRepository.findFirstByLocationOrderByCreatedAtDesc(location)
        )
    }
}