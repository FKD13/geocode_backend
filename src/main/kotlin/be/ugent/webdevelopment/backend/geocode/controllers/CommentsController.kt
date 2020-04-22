package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.CommentWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Comment
import be.ugent.webdevelopment.backend.geocode.services.CommentsService
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/comments")
@JsonView(View.PublicDetail::class)
class CommentsController(val commentsService: CommentsService, val jwtService: JWTAuthenticator) {

    @GetMapping("/{commentId}")
    fun getCommentById(@PathVariable commentId: Int): Comment {
        return commentsService.getCommentById(commentId)
    }

    @PatchMapping("/{commentId}")
    fun updateCommentById(@PathVariable commentId: Int, @RequestBody comment: CommentWrapper,
                          request: HttpServletRequest, response: HttpServletResponse) {
        commentsService.updateComment(jwtService.tryAuthenticate(request), commentId, comment)
    }

    @DeleteMapping("/{commentId}")
    fun deleteCommentById(@PathVariable commentId: Int,
                          request: HttpServletRequest, response: HttpServletResponse) {
        commentsService.deleteCommentById(jwtService.tryAuthenticate(request), commentId)
    }
}