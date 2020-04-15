package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
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
        val container = ExceptionContainer()
        //TODO checken of dat men hier effectief mag inchecken? Location zou activated moeten zijn
        val location = locationRepository.findByVisitSecret(visitSecret.toString())
        location.ifPresentOrElse({
            checkInRepository.saveAndFlush(
                    CheckIn(creator = user, location = location.get(), time = Date.from(Instant.now()))
            )
        }, {
            container.addException(PropertyException("visitSecret", "VisitSecret is not linked to any location."))
        })
        container.throwIfNotEmpty()
    }

    fun getByVisitSecret(visitSecret: UUID): Location {
        val container = ExceptionContainer()
        val location = locationRepository.findByVisitSecret(visitSecret = visitSecret.toString())
        if (location.isEmpty) {
            container.addException(PropertyException("visitSecret", "VisitSecret is not linked to any location."))
        }
        container.throwIfNotEmpty()
        return location.get()
    }

    fun getVisitsBySecretId(secret_id: UUID): List<CheckIn> {
        val container = ExceptionContainer()
        val location = locationRepository.findBySecretId(secret_id = secret_id.toString())
        if (location.isEmpty) {
            container.addException(PropertyException("secret_id", "Secret id is not linked to any location."))
        }
        container.throwIfNotEmpty()
        return checkInRepository.findAllByLocationOrderByTime(location = location.get())
    }

    fun getVisitsByUser(user: User): List<CheckIn> {
        return checkInRepository.findAllByCreator(user)
    }

    fun getVisitsByUserForLocation(user: User, secret_id: UUID): List<CheckIn> {
        val container = ExceptionContainer()
        val location = locationRepository.findBySecretId(secret_id = secret_id.toString())
        if (location.isEmpty) {
            container.addException(PropertyException("secret_id", "Secret id is not linked to any location."))
        }
        container.throwIfNotEmpty()
        return checkInRepository.findAllByLocationAndCreator(location.get(), user)
    }

}