package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.UserTour
import org.springframework.data.jpa.repository.JpaRepository

interface UserTourRepository : JpaRepository<UserTour, Int>