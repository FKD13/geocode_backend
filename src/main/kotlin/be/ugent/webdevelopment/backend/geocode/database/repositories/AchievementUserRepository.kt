package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.AchievementUser
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface AchievementUserRepository : JpaRepository<AchievementUser, Int> {
    fun findAllByUser(user: User): List<AchievementUser>
}