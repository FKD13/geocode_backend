package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Image
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ImageRepository : JpaRepository<Image, Int> {
    fun findByResourcePath(path: String): Optional<Image>
}