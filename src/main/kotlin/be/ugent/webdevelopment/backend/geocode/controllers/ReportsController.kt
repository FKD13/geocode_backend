package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ReportsWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Report
import be.ugent.webdevelopment.backend.geocode.services.ImageService
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.ReportService
import com.fasterxml.jackson.annotation.JsonView
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/reports")
@JsonView(View.AdminDetail::class)
class ReportsController(
        var imageService: ImageService,
        var reportService: ReportService,
        var jwtAuthenticator: JWTAuthenticator
) {

    @GetMapping("/{reportId}")
    fun getReportById(@PathVariable reportId: Int, request: HttpServletRequest, response: HttpServletResponse): Report {
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

    @PostMapping("/image")
    fun uploadImage(@RequestBody image: MultipartFile, request: HttpServletRequest, response: HttpServletResponse): Int {
        return imageService.saveImageFile(image)
    }


    @GetMapping("/image/{id}")
    fun getImagesForTesting(@PathVariable id: Int, request: HttpServletRequest, response: HttpServletResponse) {
        val image = imageService.getImages(id)
        val byteArray = ByteArray(image.image.size)
        var i = 0

        for (wrappedByte in image.image) {
            byteArray[i++] = wrappedByte //auto unboxing
        }

        response.contentType = image.contentType
        val inputStream: InputStream = ByteArrayInputStream(byteArray)
        IOUtils.copy(inputStream, response.outputStream)
    }

}