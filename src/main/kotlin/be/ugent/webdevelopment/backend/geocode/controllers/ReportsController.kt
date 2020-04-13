package be.ugent.webdevelopment.backend.geocode.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/reports")
class ReportsController {

    @GetMapping("/{reportId}")
    fun getReportById(@PathVariable reportId: Int) {
        //TODO
    }

    @PatchMapping("/{reportId}")
    fun updateReportById(@PathVariable reportId: Int, request: HttpServletRequest, response: HttpServletResponse) {
        //TODO
    }

    @GetMapping
    fun getReports() {
        //TODO
    }
}