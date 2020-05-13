package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserTourRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class AchievementManager(
        checkInRepository: CheckInRepository,
        locationRepository: LocationRepository,
        userTourRepository: UserTourRepository,
        resourceLoader: ResourceLoader,
        imageRepository: ImageRepository
) {
    val achievements = mapOf(
            TypeAchievement.COUNTRY to CountryAchievement(imageRepository, resourceLoader, checkInRepository),
            TypeAchievement.COUNTRYCOUNT to CountryCountAchievement(imageRepository, resourceLoader, checkInRepository),
            TypeAchievement.LOCATIONSCOUNT to LocationCountAchievement(imageRepository, resourceLoader, checkInRepository),
            TypeAchievement.LOCATIONSCREATECOUNT to LocationCreateCountAchievement(imageRepository, resourceLoader, locationRepository),
            TypeAchievement.TOURSCREATECOUNT to TourCountAchievement(imageRepository, resourceLoader, userTourRepository)
    )

    fun getAchievement(type: TypeAchievement): AbstractAchievement {
        return achievements[type] ?: throw RuntimeException("Unkown Achievement Type.")
    }
}