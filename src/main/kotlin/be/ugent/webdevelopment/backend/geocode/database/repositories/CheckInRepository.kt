package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import org.springframework.data.jpa.repository.JpaRepository

interface CheckInRepository : JpaRepository<CheckIn, Int>{
    fun findAllByLocationOrderByTime(location :Location) : List<CheckIn>
}