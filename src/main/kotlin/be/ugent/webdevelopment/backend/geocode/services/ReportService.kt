package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ReportsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.Report
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ReportRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class ReportService {

    @Autowired
    lateinit var reportRepository: ReportRepository

    @Autowired
    lateinit var locationRepository: LocationRepository

    @Autowired
    lateinit var imageRepository: ImageRepository

    @Autowired
    lateinit var imageService: ImageService

    @Autowired
    lateinit var jwtAuthenticator: JWTAuthenticator

    fun getById(reportId: Int): Report {
        val report = reportRepository.findById(reportId)
        if (report.isPresent) {
            return report.get()
        } else {
            throw GenericException("The ID: $reportId, is not linked to any report in the database.")
        }
    }

    fun checkAdmin(request: HttpServletRequest) {
        if (!(jwtAuthenticator.tryAuthenticate(request).admin)) {
            throw GenericException("You are not an admin, you may not access this endpoint.")
        }
    }

    fun getAll(): List<Report> {
        return reportRepository.findAll()
    }

    fun updateReport(reportId: Int, reportsWrapper: ReportsWrapper) {
        val report = reportRepository.findById(reportId)
        if (report.isPresent) {
            val container = ExceptionContainer()
            reportsWrapper.imageId.ifPresent { imageService.checkImageId("imageId", it, container) }
            reportsWrapper.reason.ifPresent {
                if (it.length < 4 || it.length > 2048) {
                    container.addException(PropertyException("reason", "Reason should be at least 4 characters and less than 2048 characters."))
                }
            }
            container.throwIfNotEmpty()
            reportsWrapper.imageId.ifPresent {
                report.get().image = imageRepository.findById(reportsWrapper.imageId.get()).get()
            }
            reportsWrapper.reason.ifPresent {
                report.get().reason = it
            }
            reportsWrapper.resolved.ifPresent {
                report.get().resolved = it
            }
            reportRepository.saveAndFlush(report.get())
        } else {
            throw GenericException("The reportId: $reportId, is not linked to any report in the database.")
        }
    }

    fun create(user: User, secretId: UUID, reportsWrapper: ReportsWrapper): Report {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isPresent) {
            val container = ExceptionContainer()
            reportsWrapper.imageId.ifPresentOrElse({
                imageService.checkImageId("imageId", it, container)
            }, {})
            reportsWrapper.reason.ifPresentOrElse({
                if (it.length < 4 || it.length > 2048) {
                    container.addException(PropertyException("reason", "Reason should be at least 5 characters and less than 2048 characters."))
                }
            }, {
                container.addException(PropertyException("reason", "The reason is an expected value."))
            })
            container.throwIfNotEmpty()

            var image: Image? = null
            reportsWrapper.imageId.ifPresent {
                image = imageRepository.findById(it).orElseGet { null }
            }
            return reportRepository.saveAndFlush(Report(
                    createdAt = Date.from(Instant.now()),
                    image = image,
                    creator = user,
                    location = location.get(),
                    reason = reportsWrapper.reason.get(),
                    resolved = false
            ))
        } else {
            throw GenericException("The secretId: $secretId, is not linked to any location in the database.")
        }
    }

    fun getByLocation(secretId: UUID): List<Report> {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isPresent) {
            return reportRepository.findAllByLocation(location = location.get())
        } else {
            throw GenericException("The secretId: $secretId, is not linked to any location in the database.")
        }
    }

}
