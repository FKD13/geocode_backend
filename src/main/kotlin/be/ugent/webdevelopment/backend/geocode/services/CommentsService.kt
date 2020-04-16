package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.CommentWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Comment
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.CommentRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class CommentsService {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var locationRepository: LocationRepository

    fun getCommentsBySecretId(secretId: UUID): List<Comment> {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isEmpty) {
            throw GenericException("Secret id is not linked to any location.")
        }
        return commentRepository.findAllByLocation(location = location.get())
    }

    fun createComment(user: User, secretId: UUID, comment: CommentWrapper) {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isEmpty) {
            throw GenericException("Secret id is not linked to any location.")
        }
        val commentString: String = comment.comment.orElseGet { "" }
        commentRepository.saveAndFlush(
                Comment(creator = user, location = location.get(), createdAt = Date.from(Instant.now()), comment = commentString))
    }

    fun getCommentById(id: Int): Comment {
        val comment = commentRepository.findById(id)
        if (comment.isEmpty) {
            throw GenericException("Comment with id = $id was not found.")
        } else {
            return comment.get()
        }
    }

    fun updateComment(user: User, id: Int, comment: CommentWrapper) {
        commentRepository.findById(id).ifPresentOrElse({
            if (it.creator.id != user.id) {
                throw GenericException("The currently logged in user did not create this comment and can therefor not edit it.")
            } else {
                it.comment = comment.comment.orElseGet { "" }
            }
        }, {
            throw GenericException("Comment with id = $id was not found.")
        })
    }

    fun deleteCommentById(user: User, id: Int) {
        commentRepository.findById(id).ifPresentOrElse({
            if (it.creator.id != user.id) {
                throw GenericException("The currently logged in user did not create this comment and can therefor not delete it.")
            } else {
                commentRepository.delete(it)
            }
        }, {
            throw GenericException("Comment with id = $id was not found.")
        })
    }
}