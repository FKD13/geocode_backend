package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import org.springframework.core.io.ResourceLoader

class CountryCountAchievement(
        imageRepository: ImageRepository,
        resourceLoader: ResourceLoader,
        private val checkInRepository: CheckInRepository
) : AbstractAchievement(imageRepository, resourceLoader) {

    override fun achieved(user: User, achievement: Achievement): Boolean {
        return checkInRepository.findAllByCreator(user)
                .map { it.location.country.toLowerCase() }.distinct().count() >= achievement.value
                ?: throw IllegalStateException("This value can not be null.")
    }

    override fun storeInternal(template: AchievementTemplate, image: Image, repository: AchievementRepository) {
        if (template.value is Int) {
            repository.save(Achievement(
                    title = template.title,
                    description = template.description,
                    image = image,
                    type = TypeAchievement.COUNTRYCOUNT,
                    value = template.value
            ))
        } else {
            throw RuntimeException("value not a integer.")
        }
    }
}