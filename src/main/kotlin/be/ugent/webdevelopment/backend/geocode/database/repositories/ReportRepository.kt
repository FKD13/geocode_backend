package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Report
import org.springframework.data.repository.CrudRepository

interface ReportRepository : CrudRepository<Report, Long>