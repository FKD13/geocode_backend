package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.JsonLDSerializable
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.AchievementRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractAchievement(
        private val imageRepository: ImageRepository
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
    private fun loadImage(resourcePath: String) : Image {
        val fileUri = javaClass.classLoader.getResource(resourcePath)?.file
        if (fileUri == null) {
            throw RuntimeException("resource at $resourcePath not found")
        } else {
            val optImage = imageRepository.findByResourcePath(resourcePath)
            return if (optImage.isEmpty) {
                val bytes = Files.readAllBytes(Path.of(fileUri))
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
}