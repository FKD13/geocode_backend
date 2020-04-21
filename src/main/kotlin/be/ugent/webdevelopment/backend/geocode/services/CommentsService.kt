package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.CommentWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Comment
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.CommentRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class CommentsService {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var locationRepository: LocationRepository

    private fun checkCommentMessage(message: String, exceptionContainer: ExceptionContainer) {
        if (message.length !in 4..1024) {
            exceptionContainer.addException(PropertyException(
                    field = "message",
                    message = "message should be at least 4 and at most 1024 characters long"
            ))
        }
    }

    fun getCommentsBySecretId(secretId: UUID): List<Comment> {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isEmpty) {
            throw GenericException("Secret id is not linked to any location.")
        }
        return commentRepository.findAllByLocation(location = location.get())
    }

    fun createComment(user: User, secretId: UUID, comment: CommentWrapper): Comment {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isEmpty) {
            throw GenericException("Secret id is not linked to any location.")
        }
        val commentString: String = comment.message.orElseGet { "" }

        val ec = ExceptionContainer(code = HttpStatus.UNPROCESSABLE_ENTITY)
        checkCommentMessage(commentString, ec)
        ec.ifNotEmpty {
            throw ec.also { it.addException(GenericException("Could not create comment.")) }
        }

        return commentRepository.saveAndFlush(
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
                throw GenericException("The currently logged in user did not create this comment and can therefore not edit it.")
            } else {
                val comment = comment.message.orElseGet { "" }
                val ec = ExceptionContainer(code = HttpStatus.UNPROCESSABLE_ENTITY)
                checkCommentMessage(comment, ec)
                ec.ifEmptyOrElse({
                    throw ec.also { container -> container.addException(GenericException("Could not update comment.")) }
                },{
                    it.comment = comment
                    commentRepository.saveAndFlush(it)
                })
            }
        }, {
            throw GenericException("Comment with id = $id was not found.")
        })
    }

    fun deleteCommentById(user: User, id: Int) {
        commentRepository.findById(id).ifPresentOrElse({
            if (it.creator.id != user.id) {
                throw GenericException("The currently logged in user did not create this comment and can therefore not delete it.")
            } else {
                commentRepository.delete(it)
            }
        }, {
            throw GenericException("Comment with id = $id was not found.")
        })
    }
}