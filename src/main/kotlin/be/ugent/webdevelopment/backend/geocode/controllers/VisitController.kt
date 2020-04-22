package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.services.VisitsService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/checkin")
@JsonView(View.AdminDetail::class)
class VisitController(
        var visitsService: VisitsService
) {


    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int, request: HttpServletRequest, response: HttpServletResponse): CheckIn {
        return visitsService.getById(id)
    }
}