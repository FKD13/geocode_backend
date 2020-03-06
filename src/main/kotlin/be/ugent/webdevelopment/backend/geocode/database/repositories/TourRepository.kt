package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import org.springframework.data.jpa.repository.JpaRepository

interface TourRepository : JpaRepository<Tour, Int>