package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserTourRepository
import org.springframework.core.io.ResourceLoader

class TourCountAchievement(
        imageRepository: ImageRepository,
        resourceLoader: ResourceLoader,
        private val userTourRepository: UserTourRepository
) : AbstractAchievement(imageRepository, resourceLoader) {
    override fun achieved(user: User, achievement: Achievement): Boolean {
        return userTourRepository.findAllByUser(user).distinct().count() >= achievement.value
                ?: throw IllegalStateException("This value can not be null.")
    }

    override fun storeInternal(template: AchievementTemplate, image: Image, repository: AchievementRepository) {
        if (template.value is Int) {
            repository.saveAndFlush(Achievement(
                    name = template.title,
                    description = template.description,
                    image = image,
                    type = TypeAchievement.TOURSCREATECOUNT,
                    value = template.value
            ))
        } else {
            throw RuntimeException("value not a integer.")
        }
    }
}