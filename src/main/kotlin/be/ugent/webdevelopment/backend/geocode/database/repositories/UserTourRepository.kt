package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.UserTour
import org.springframework.data.repository.CrudRepository

interface UserTourRepository : CrudRepository<UserTour, Long>