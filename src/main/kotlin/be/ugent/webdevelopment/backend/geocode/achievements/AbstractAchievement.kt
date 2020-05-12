package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.JsonLDSerializable
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader

abstract class AbstractAchievement(
        private val imageRepository: ImageRepository,
        private val resourceLoader: ResourceLoader
) : JsonLDSerializable() {
    abstract fun achieved(user: User, achievement: Achievement): Boolean
    abstract fun storeInternal(template: AchievementTemplate, image: Image, repository: AchievementRepository)

    /**
     *
     */
    fun store(template: AchievementTemplate, repository: AchievementRepository) {
        val optAchievement = repository.findAllByTitle(template.title)
        if (optAchievement.isEmpty) {
            val image = loadImage(template.image)
            storeInternal(template, image, repository)
        } else {
            LoggerFactory.getLogger(javaClass).debug("Skipping achievement ${template.title}, already in database.")
        }
    }

    /**
     * Load Image from recource folder
     */
    private fun loadImage(resourcePath: String): Image {
        val optImage = imageRepository.findByResourcePath(resourcePath)
        return if (optImage.isEmpty) {
            val bytes = resourceLoader.getResource("classpath:$resourcePath").inputStream.readBytes()
            imageRepository.save(Image(
                    image = bytes.toTypedArray(),
                    contentType = "image/svg",
                    resourcePath = resourcePath
            ))
        } else {
            optImage.get()
        }
    }
}