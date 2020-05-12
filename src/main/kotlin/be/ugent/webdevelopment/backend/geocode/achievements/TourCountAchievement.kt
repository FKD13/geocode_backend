package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.TourRepository

class TourCountAchievement(
        imageRepository: ImageRepository,
        private val tourRepository: TourRepository
) : AbstractAchievement(imageRepository) {
    override fun achieved(user: User, achievement: Achievement): Boolean {
        return tourRepository.getAllByCreator(user).count() >= achievement.value!!
    }
    override fun storeInternal(template: AchievementTemplate, image: Image, repository: AchievementRepository) {
        if (template.value is Int) {
            repository.save(Achievement(
                    title = template.title,
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