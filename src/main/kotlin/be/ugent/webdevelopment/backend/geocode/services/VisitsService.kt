package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedLocationWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.models.UserTour
import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.TourRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserTourRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class VisitsService {

    @Autowired
    lateinit var checkInRepository: CheckInRepository

    @Autowired
    lateinit var locationRepository: LocationRepository

    @Autowired
    lateinit var tourRepository: TourRepository

    @Autowired
    lateinit var userTourRepository: UserTourRepository

    @Autowired
    lateinit var locationsService: LocationsService

    @Autowired
    lateinit var achievementService: AchievementService

    @Async
    fun checkTours(user: User, location: Location) {
        tourRepository.getAllByActiveTrueAndListedTrue().filter { it.locations.contains(location) }.apply {
            val userTours = userTourRepository.findAllByUser(user)
            //Check all the tours which start with this location.
            this.filter { it.locations[0] == location }.apply {
                //The current location is de eerste in de lijst van locations van deze tours.
                this.forEach {
                    if (!(userTours.map { it.tour }.contains(it))) {
                        //User is not niet begonnen aan deze tour dus maken we een nieuwe UserTour aan
                        userTourRepository.saveAndFlush(UserTour(
                                user = user,
                                tour = it,
                                createdAt = Date.from(Instant.now())
                                //De rest mag op de default waarden blijven staan
                        ))
                    }
                }
            }
            //Check all the tours that don't start with this location but have it somewhere in the list.
            this.filter { it.locations[0] != location }.apply {
                this.forEach {
                    if (userTours.map { it.tour }.contains(it)) {
                        //De user is al aan deze tour begonnen.
                        val theCurrentUserTour = userTourRepository.findAllByTour(it)
                        if ((!theCurrentUserTour.completed) && it.locations[theCurrentUserTour.amountLocationsVisited]
                                == location) {
                            //The current location is de volgende dat de user moet doen in de tour.
                            theCurrentUserTour.amountLocationsVisited++
                            if (theCurrentUserTour.amountLocationsVisited.equals(it.locations.size)) {
                                theCurrentUserTour.completed = true
                            }
                            userTourRepository.saveAndFlush(theCurrentUserTour)
                        }
                    }
                }
            }
        }
        achievementService.validateAchievements(user)
    }

    fun visit(user: User, visitSecret: UUID): ExtendedLocationWrapper {
        val location = locationRepository.findByVisitSecretAndActive(visitSecret.toString(), active = true)

        location.ifPresentOrElse({

            val date = Calendar.getInstance().apply {
                add(Calendar.HOUR, -24)
            }

            if (!user.admin) {
                if (checkInRepository.findAllByCreatorAndLocationAndCreatedAtAfter(user, it, date = date.time).isNotEmpty()) {
                    throw GenericException("You can only checkin once per location every day.")
                }
            }

            checkInRepository.saveAndFlush(
                    CheckIn(creator = user, location = location.get(), createdAt = Date.from(Instant.now()))
            )
            checkTours(user, it)
        }, {
            throw GenericException("VisitSecret is not linked to any location or the location is not active.")
        })
        return ExtendedLocationWrapper(location.get(), locationsService.getRating(location.get()))
    }

    fun getByVisitSecret(visitSecret: UUID): ExtendedLocationWrapper {
        val location = locationRepository.findByVisitSecret(visitSecret = visitSecret.toString())
        if (location.isEmpty) {
            throw GenericException("VisitSecret is not linked to any location.")
        }
        return ExtendedLocationWrapper(location.get(), locationsService.getRating(location.get()))
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

    fun getById(id: Int): CheckIn {
        return checkInRepository.findById(id).orElseThrow { GenericException("Checkin with id = $id was not found in the database.") }
    }
}