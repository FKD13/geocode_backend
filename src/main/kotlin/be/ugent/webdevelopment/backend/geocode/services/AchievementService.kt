package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.achievements.AchievementManager
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
    fun validateAchievements(user: User) {
        //TODO
    }
}