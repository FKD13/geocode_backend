package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserTourRepository
import org.springframework.stereotype.Component

@Component
class AchievementManager(
        checkInRepository: CheckInRepository,
        locationRepository: LocationRepository,
        userTourRepository: UserTourRepository,
        imageRepository: ImageRepository
) {
    val achievements = mapOf(
            TypeAchievement.COUNTRY to CountryAchievement(imageRepository, checkInRepository),
            TypeAchievement.COUNTRYCOUNT to CountryCountAchievement(imageRepository, checkInRepository),
            TypeAchievement.LOCATIONSCOUNT to LocationCountAchievement(imageRepository, checkInRepository),
            TypeAchievement.LOCATIONSCREATECOUNT to LocationCreateCountAchievement(imageRepository, locationRepository),
            TypeAchievement.TOURSCREATECOUNT to TourCountAchievement(imageRepository, userTourRepository)
    )

    fun getAchievement(type: TypeAchievement): AbstractAchievement {
        return achievements[type] ?: throw RuntimeException("Unkown Achievement Type.")
    }
}