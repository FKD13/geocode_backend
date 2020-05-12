package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository

class CountryAchievement(imageRepository: ImageRepository) : AbstractAchievement(imageRepository) {
    override fun achieved(user: User, achievement: Achievement): Boolean {
        TODO("Not yet implemented")
    }

    override fun storeInternal(template: AchievementTemplate, image: Image, repository: AchievementRepository) {
        if (template.value is String) {
            repository.save(Achievement(
                    title = template.title,
                    description = template.description,
                    image = image,
                    type = TypeAchievement.COUNTRY,
                    stringValue = template.value
            ))
        } else {
            throw RuntimeException("value not a string.")
        }
    }
}