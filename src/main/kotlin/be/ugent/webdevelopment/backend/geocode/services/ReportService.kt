package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedReportsWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ReportsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Report
import be.ugent.webdevelopment.backend.geocode.database.models.User
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
    lateinit var imageService: ImageService

    @Autowired
    lateinit var jwtAuthenticator: JWTAuthenticator

    fun getById(reportId: Int): ExtendedReportsWrapper {
        val report = reportRepository.findById(reportId)
        if (report.isPresent) {
            return ExtendedReportsWrapper(report.get(), imageService.getUrlForImage("report/image", report.get().imageId))
        } else {
            throw GenericException("The ID: $reportId, is not linked to any report in the database.")
        }
    }

    fun checkAdmin(request: HttpServletRequest) {
        if (!(jwtAuthenticator.tryAuthenticate(request).admin)) {
            throw GenericException("You are not an admin, you may not access this endpoint.")
        }
    }

    fun getAll(): List<ExtendedReportsWrapper> {
        return reportRepository.findAll().map { ExtendedReportsWrapper(it, imageService.getUrlForImage("report/image", it.imageId)) }
    }

    fun updateReport(reportId: Int, reportsWrapper: ReportsWrapper) {
        val report = reportRepository.findById(reportId)
        if (report.isPresent) {
            val container = ExceptionContainer()
            reportsWrapper.imageId.ifPresent { imageService.checkImageId("imageId", it, container) }
            reportsWrapper.reason.ifPresent {
                if (it.length < 5) {
                    container.addException(PropertyException("reason", "Reason should be at least 5 characters."))
                }
            }
            container.throwIfNotEmpty()
            reportsWrapper.imageId.ifPresent {
                report.get().imageId = it
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

    fun create(user: User, secretId: UUID, reportsWrapper: ReportsWrapper): ExtendedReportsWrapper {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isPresent) {
            val container = ExceptionContainer()
            reportsWrapper.imageId.ifPresentOrElse({
                imageService.checkImageId("imageId", it, container)
            }, {})
            reportsWrapper.reason.ifPresentOrElse({
                if (it.length < 5) {
                    container.addException(PropertyException("reason", "The reason should be at least 5 characters."))
                }
            }, {
                container.addException(PropertyException("reason", "The reason is an expected value."))
            })
            reportsWrapper.resolved.ifPresentOrElse({}, {
                container.addException(PropertyException("resolved", "Resolved is an expected value."))
            })
            container.throwIfNotEmpty()
            return ExtendedReportsWrapper(reportRepository.saveAndFlush(Report(
                    createdAt = Date.from(Instant.now()),
                    imageId = reportsWrapper.imageId.get(),
                    creator = user,
                    location = location.get(),
                    reason = reportsWrapper.reason.get(),
                    resolved = reportsWrapper.resolved.orElse(false)
            )), imageService.getUrlForImage("report/image", reportsWrapper.imageId.get()))
        } else {
            throw GenericException("The secretId: $secretId, is not linked to any location in the database.")
        }
    }

    fun getByLocation(secretId: UUID): List<ExtendedReportsWrapper> {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isPresent) {
            return reportRepository.findAllByLocation(location = location.get()).map {
                ExtendedReportsWrapper(it, imageService.getUrlForImage("report/image", it.imageId))
            }
        } else {
            throw GenericException("The secretId: $secretId, is not linked to any location in the database.")
        }
    }

}
