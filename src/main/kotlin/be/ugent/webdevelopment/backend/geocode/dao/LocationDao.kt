package be.ugent.webdevelopment.backend.geocode.dao

import be.ugent.webdevelopment.backend.geocode.model.Location
import java.util.*
import kotlin.random.Random

interface LocationDao {

    fun insertLocation(id: UUID, location: Location) : UUID
    fun insertLocation(location: Location): UUID{
        val id: UUID = UUID.randomUUID()
        return insertLocation(id, location)
    }

    fun getAllLocations(): List<Location>

    fun getLocationById(id: UUID): Location?

    fun updateLocation(id: UUID, location: Location): Int

    fun deleteLocation(id: UUID): Int
}