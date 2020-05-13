package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

/**
 * Load achievments to db if this has not happened already.
 */
@Component
class AchievementLoader(
        val achievementRepository: AchievementRepository,
        val achievementManager: AchievementManager,
        val locationRepository: LocationRepository
) : InitializingBean {

    private val achievements = mapOf(
            TypeAchievement.COUNTRY to listOf(
                    AchievementTemplate(
                            "Brexit", "Visit GB", "tower.png", "Great Britan"
                    )
            ),
            TypeAchievement.COUNTRYCOUNT to listOf(
                    AchievementTemplate(
                            "County Hopper", "Visit 5 countries", "space.png", 5
                    )
            ),
            TypeAchievement.TOURSCREATECOUNT to listOf(
                    AchievementTemplate(
                            "Tour Breeder", "Create 5 tours", "direction.png", 5
                    )
            ),
            TypeAchievement.LOCATIONSCOUNT to listOf(
                    AchievementTemplate(
                            "Hiker", "Visit 10 locations", "fashion.png", 10
                    )
            ),
            TypeAchievement.LOCATIONSCREATECOUNT to listOf(
                    AchievementTemplate(
                            "Locations Breeder", "Create 5 locations", "street-map.png", 5
                    )
            )
    )

    override fun afterPropertiesSet() {
        for (i in achievements) {
            val achievement = achievementManager.getAchievement(i.key)
            i.value.map {
                achievement.store(it, achievementRepository)
            }
        }
    }
}

