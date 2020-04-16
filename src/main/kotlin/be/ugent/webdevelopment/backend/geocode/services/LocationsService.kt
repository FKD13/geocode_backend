package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedLocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRatingRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.UserRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import be.ugent.webdevelopment.backend.geocode.utils.CountryUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class LocationsService {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var locationRatingRepository: LocationRatingRepository

    private val descriptionTagsPattern = Pattern.compile("<\\s*(?!li|ul|p|b|i|u|img|br|h1|h2|h3)([^<>\\s]*)([^<>]*)>(.*)<\\s*/\\s*\\1\\s*>", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE)
    private val attributesPattern = Pattern.compile("<[^<>]*\\s+(?!src|height|width)([^<>=]+)=[^<>]*", Pattern.CASE_INSENSITIVE + Pattern.MULTILINE)
    private val countryUtil = CountryUtil()

    fun findAll(): List<ExtendedLocationWrapper> {
        return locationRepository.findAllByListedAndActive(listed = true, active = true).map { ExtendedLocationWrapper(it, getRating(it)) }
    }

    fun findBySecretId(secretId: UUID): Location {
        val loc: Optional<Location> = locationRepository.findBySecretId(secretId.toString())
        loc.ifPresentOrElse({}, { throw GenericException("Location that corresponds to this secret id was not found.") })
        return loc.get()
    }

    private fun getRating(location: Location): Double {
        val ratings = locationRatingRepository.findAllByLocation(location).map { locationRating -> locationRating.rating }
        if (ratings.isEmpty()) return 0.0
        return ratings.sum().div(ratings.size.toDouble())
    }


    fun findAllByUser(user: User): List<LocationWrapper> {
        return locationRepository.findByCreator(user).map { LocationWrapper(it) }
    }

    fun checkLat(lat: Double, container: ExceptionContainer) {
        if (lat > 90) {
            container.addException(PropertyException("latitude", "Latitude can not be bigger than 90."))
        } else if (lat < -90) {
            container.addException(PropertyException("latitude", "Latitude can not be smaller than -90."))
        }
    }

    fun checkLon(lon: Double, container: ExceptionContainer) {
        if (lon > 180) {
            container.addException(PropertyException("longitude", "Longitude can not be bigger than 180."))
        } else if (lon < -180) {
            container.addException(PropertyException("longitude", "Longitude can not be smaller than -180."))
        }
    }

    fun checkName(name: String, container: ExceptionContainer) {
        if (name.length > 255) {
            container.addException(PropertyException("name", "Name can not be bigger than 255 characters."))
        } else if (name.length < 3) {
            container.addException(PropertyException("name", "Name can not be smaller than 3 characters."))
        }
    }

    fun checkDescription(description: String, container: ExceptionContainer) {
        if (attributesPattern.matcher(description).matches()) {
            container.addException(PropertyException("description", "Description has html attributes that are not valid."))
        }
        if (descriptionTagsPattern.matcher(description).matches()) {
            container.addException(PropertyException("description", "Description has html tags that are not valid."))
        }
        /* li ul p b i u img br */
        description.replace(Regex("style=[^>]*>"), ">") //this replaces all the style elements
    }

    fun checkId(creatorId: Int, container: ExceptionContainer) {
        userRepository.findById(creatorId).ifPresentOrElse({}, { container.addException(PropertyException("creatorId", "Creator with creatorId = $creatorId does not exist.")) })
    }

    private fun checkCountry(country: String, exceptionContainer: ExceptionContainer) {
        if (countryUtil.countries.contains(country.toLowerCase()).not()) {
            exceptionContainer.addException(PropertyException(
                    field = "country",
                    message = "This is not a valid country"
            ))
        }
    }

    fun create(resource: LocationsWrapper): UUID {
        val container = ExceptionContainer()

        resource.longitude.ifPresentOrElse({ checkLon(resource.longitude.get(), container) }, { container.addException(PropertyException("longitude", "Longitude is an expected value.")) })
        resource.latitude.ifPresentOrElse({ checkLat(resource.latitude.get(), container) }, { container.addException(PropertyException("latitude", "Latitude is an expected value.")) })
        resource.name.ifPresentOrElse({ checkName(resource.name.get(), container) }, { container.addException(PropertyException("name", "Name is an expected value.")) })
        resource.description.ifPresentOrElse({ checkDescription(resource.description.get(), container) }, { container.addException(PropertyException("description", "Description is an expected value.")) })
        resource.creatorId.ifPresentOrElse({ checkId(resource.creatorId.get(), container) }, { container.addException(PropertyException("creatorId", "CreatorId is an expected value.")) })
        resource.listed.ifPresentOrElse({}, { container.addException(PropertyException("listed", "Listed is an expected value.")) })
        resource.country.ifPresentOrElse({ checkCountry(resource.country.get(), container) }, { container.addException(PropertyException("country", "Country is an expected value.")) })
        resource.address.ifPresentOrElse({}, { container.addException(PropertyException("address", "Address is an expected value.")) })

        if (container.isEmpty().not()) {
            throw container.also { it.addException(GenericException("Location could not be created")) }
        }

        val loc = Location(
                longitude = resource.longitude.get(),
                latitude = resource.latitude.get(),
                secretId = UUID.randomUUID().toString(),
                visitSecret = UUID.randomUUID().toString(),
                listed = resource.listed.get(),
                name = resource.name.get(),
                description = resource.description.get(),
                creator = userRepository.findById(resource.creatorId.get()).get(),
                country = resource.country.get(),
                address = resource.address.get(),
                active = resource.active.orElseGet { false }
        )
        return UUID.fromString(locationRepository.saveAndFlush(loc).secretId)
    }

    fun update(secretId: UUID, resource: LocationWrapper) {
        val container = ExceptionContainer()

        resource.longitude.ifPresent { checkLon(resource.longitude.get(), container) }
        resource.latitude.ifPresent { checkLat(resource.latitude.get(), container) }
        resource.name.ifPresent { checkName(resource.name.get(), container) }
        resource.description.ifPresent { checkDescription(resource.description.get(), container) }
        resource.creatorId.ifPresent { checkId(resource.creatorId.get(), container) }

        container.throwIfNotEmpty()

        locationRepository.findBySecretId(secretId = secretId.toString()).ifPresentOrElse({
            val location: Location = it
            resource.longitude.ifPresent { location.longitude = resource.longitude.get() }
            resource.latitude.ifPresent { location.latitude = resource.latitude.get() }
            resource.name.ifPresent { location.name = resource.name.get() }
            resource.description.ifPresent { location.description = resource.description.get() }
            resource.creatorId.ifPresent { location.creator = userRepository.findById(resource.creatorId.get()).get() }
            resource.active.ifPresent { location.active = resource.active.get() }
            locationRepository.saveAndFlush(location)
        }, {
            throw GenericException("Location with secretId=$secretId does not exist in the database.")
        })
    }

    fun deleteById(user: User, secretId: UUID) {
        locationRepository.findBySecretId(secretId.toString()).ifPresentOrElse({
            if (it.creator.id != user.id) {
                throw GenericException("The currently logged in user did not create this location and can therefore not delete it.")
            } else {
                locationRepository.delete(it)
                locationRepository.flush()
            }
        }, {
            throw GenericException("Location with secretId= $secretId was not found in the database.")
        })

    }

}