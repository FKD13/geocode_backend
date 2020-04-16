package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class VisitsService {

    @Autowired
    lateinit var checkInRepository: CheckInRepository

    @Autowired
    lateinit var locationRepository: LocationRepository

    fun visit(user: User, visitSecret: UUID) {
        val location = locationRepository.findByVisitSecret(visitSecret.toString())
        location.ifPresentOrElse({
            if (it.active){
                checkInRepository.saveAndFlush(
                        CheckIn(creator = user, location = location.get(), createdAt = Date.from(Instant.now()))
                )
            }else{
                throw GenericException("The location you tried to visit is not active yet.", HttpStatus.FORBIDDEN)
            }
        }, {
            throw GenericException("VisitSecret is not linked to any location.")
        })
    }

    fun getByVisitSecret(visitSecret: UUID): Location {
        val container = ExceptionContainer()
        val location = locationRepository.findByVisitSecret(visitSecret = visitSecret.toString())
        if (location.isEmpty) {
            throw GenericException("VisitSecret is not linked to any location.")
        }
        container.throwIfNotEmpty()
        return location.get()
    }

    fun getVisitsBySecretId(secretId: UUID): List<CheckIn> {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isEmpty) {
            throw GenericException("Secret id is not linked to any location.")
        }
        return checkInRepository.findAllByLocationOrderByCreatedAt(location = location.get())
    }

    fun getVisitsByUser(user: User): List<CheckIn> {
        return checkInRepository.findAllByCreator(user)
    }

    fun getVisitsByUserForLocation(user: User, secretId: UUID): List<CheckIn> {
        val location = locationRepository.findBySecretId(secretId = secretId.toString())
        if (location.isEmpty) {
            throw GenericException("Secret id is not linked to any location.")
        }
        return checkInRepository.findAllByLocationAndCreator(location.get(), user)
    }

}