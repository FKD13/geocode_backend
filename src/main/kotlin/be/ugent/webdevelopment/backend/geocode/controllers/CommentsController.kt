package be.ugent.webdevelopment.backend.geocode.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/comments")
class CommentsController {

    @GetMapping("/{commentId}")
    fun getCommentById(@PathVariable commentId: Int) {
        //TODO
    }

    @PatchMapping("/{commentId}")
    fun updateCommentById(@PathVariable commentId: Int, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    @DeleteMapping("/{commentId}")
    fun deleteCommentById(@PathVariable commentId: Int, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }
}