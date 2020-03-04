package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import org.springframework.data.repository.CrudRepository

interface CheckInRepository : CrudRepository<CheckIn, Long>