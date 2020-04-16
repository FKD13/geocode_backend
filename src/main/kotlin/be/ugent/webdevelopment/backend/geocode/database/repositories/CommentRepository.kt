package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Comment
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Int> {
    fun findAllByLocation(location: Location): List<Comment>
}