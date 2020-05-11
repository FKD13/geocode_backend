package be.ugent.webdevelopment.backend.geocode.achievements

import org.springframework.stereotype.Component

@Component
class AchievementManager {
    val achievements = mapOf(
            TypeAchievement.COUNTRY to CountryAchievement(),
            TypeAchievement.COUNTRYCOUNT to CountryCountAchievement(),
            TypeAchievement.LOCATIONSCOUNT to LocationCountAchievement(),
            TypeAchievement.LOCATIONSCREATECOUNT to LocationCreateCountAchievement(),
            TypeAchievement.TOURSCREATECOUNT to TourCountAchievement()
    )

    fun getAchievement(type: TypeAchievement) : AbstractAchievement {
        return achievements[type] ?: throw RuntimeException("Unkown Achievement Type.")
    }
}