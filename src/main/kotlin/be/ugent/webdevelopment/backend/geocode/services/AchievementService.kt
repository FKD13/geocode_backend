package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.achievements.AchievementManager
import be.ugent.webdevelopment.backend.geocode.database.models.AchievementUser
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementUserRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class AchievementService(
        val achievementManager: AchievementManager,
        val achievementRepository: AchievementRepository,
        val achievementUserRepository: AchievementUserRepository
) {
    /**
     * Get all achievements
     */
    fun getAchievements() = achievementRepository.findAll()

    /**
     * Get All achievements by user
     */
    fun getUserAchievements(user: User) = achievementUserRepository.findAllByUser(user).map { it.achievement }

    /**
     * A function that will validate the achievements of a user
     * Should be called when creating locations, tours an checking in.
     */
    @Async
    fun validateAchievementsAsync(user: User) {
        validateAchievements(user)
    }

    fun validateAchievements(user: User) {
        val achievements = getAchievements()
        val achievedAchievements = getUserAchievements(user).distinct()
        val filtered = achievements.filter { !achievedAchievements.contains(it) }
        for (i in filtered) {
            if (achievementManager.getAchievement(i.type).achieved(user, i)) {
                achievementUserRepository.saveAndFlush(AchievementUser(user = user, achievement = i))
            }
        }
    }
}