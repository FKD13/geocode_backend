package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import org.springframework.data.repository.CrudRepository

interface TourRepository : CrudRepository<Tour, Long>