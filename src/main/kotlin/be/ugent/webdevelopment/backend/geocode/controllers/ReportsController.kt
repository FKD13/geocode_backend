package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ReportLocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ReportsWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Report
import be.ugent.webdevelopment.backend.geocode.services.ImageService
import be.ugent.webdevelopment.backend.geocode.services.ReportService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/reports")
@JsonView(View.AdminDetail::class)
class ReportsController(
        var imageService: ImageService,
        var reportService: ReportService
) {

    @GetMapping("/{reportId}")
    fun getReportById(@PathVariable reportId: Int, request: HttpServletRequest, response: HttpServletResponse):
            Report {
        reportService.checkAdmin(request)
        return reportService.getById(reportId)
    }

    @PatchMapping("/{reportId}")
    fun updateReportById(@PathVariable reportId: Int, @RequestBody report: ReportsWrapper,
                         request: HttpServletRequest, response: HttpServletResponse) {
        reportService.checkAdmin(request)
        reportService.updateReport(reportId, report)
    }

    @GetMapping
    fun getReports(request: HttpServletRequest, response: HttpServletResponse): List<Report> {
        reportService.checkAdmin(request)
        return reportService.getAll()
    }

    @GetMapping("/locations")
    fun getLocations(request: HttpServletRequest, response: HttpServletResponse): List<ReportLocationWrapper> {
        reportService.checkAdmin(request)
        return reportService.getLocations()
    }

    @PostMapping("/image")
    fun uploadImage(@RequestBody image: MultipartFile, request: HttpServletRequest, response: HttpServletResponse): Int {
        return imageService.saveImageFile(image)
    }

}