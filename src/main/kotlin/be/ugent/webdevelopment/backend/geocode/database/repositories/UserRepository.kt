package be.ugent.webdevelopment.backend.geocode.database.repositories

import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>