package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AchievementRepository : JpaRepository<Achievement, Int> {
    fun findAllByName(name: String): Optional<Achievement>
}