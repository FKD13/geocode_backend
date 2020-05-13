package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.Report
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReportRepository : JpaRepository<Report, Int> {
    fun findAllByImage(image: Image): Optional<Report>
    fun findAllByLocation(location: Location): List<Report>
    fun findAllByResolvedFalse(): List<Report>
}