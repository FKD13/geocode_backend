package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.View
import com.fasterxml.jackson.annotation.JsonView
import io.swagger.v3.core.util.Json
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/reports")
@JsonView(View.AdminDetail::class)
class ReportsController {

    @GetMapping("/{reportId}")
    fun getReportById(@PathVariable reportId: Int) {
        //TODO
    }

    @PatchMapping("/{reportId}")
    fun updateReportById(@PathVariable reportId: Int, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    @GetMapping //todo deze mag misschien public detail krijgen?
    fun getReports() {
        //TODO
    }
}