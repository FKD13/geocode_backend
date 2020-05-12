package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LeaderboardsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.JsonLDSerializable
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
class LeaderboardsService(
        val tourRepository: TourRepository,
        val locationRepository: LocationRepository,
        val checkInRepository: CheckInRepository,
        val userTourRepository: UserTourRepository,
        val userRepository: UserRepository
) {
    private fun <T : JsonLDSerializable> getLeaderboard(
            repository: JpaRepository<T, Int>,
            preFilter: (T) -> Boolean = { true },
            filter: (T, User) -> Boolean = { _, _ -> true },
            map: (T) -> Any = { it },
            limit: Int
    ): List<LeaderboardsWrapper> {
        val result = mutableListOf<LeaderboardsWrapper>()
        val list = repository.findAll().filter { preFilter.invoke(it) }
        userRepository.findAll()
                .filter { it.displayOnLeaderboards }
                .forEach {
                    result.add(LeaderboardsWrapper(
                            it, list.filter { item -> filter.invoke(item, it) }.map { item -> map.invoke(item) }.distinct().size
                    ))
                }
        return result.sortedBy { it.value }.reversed().take(limit)
    }

    fun visitedLocations(limit: Int) = getLeaderboard(
            repository = checkInRepository,
            preFilter = { it.location.active && it.location.listed },
            filter = { item, user -> item.creator == user },
            map = { it.location },
            limit = limit
    )

    fun visitedTours(limit: Int) = getLeaderboard(
            repository = userTourRepository,
            preFilter = { it.completed && it.tour.listed && it.tour.active },
            filter = { item, user -> item.user == user },
            map = { it.tour },
            limit = limit
    )

    fun visitedCountries(limit: Int) = getLeaderboard(
            repository = checkInRepository,
            preFilter = { it.location.listed && it.location.active },
            filter = { item, user -> item.creator == user },
            map = { it.location.country },
            limit = limit
    )

    fun createdLocations(limit: Int) = getLeaderboard(
            repository = locationRepository,
            preFilter = { it.listed && it.active },
            filter = { item, user -> item.creator == user },
            limit = limit
    )

    fun createdTours(limit: Int) = getLeaderboard(
            repository = tourRepository,
            preFilter = { it.listed && it.active },
            filter = { item, user -> item.creator == user },
            limit = limit
    )
}