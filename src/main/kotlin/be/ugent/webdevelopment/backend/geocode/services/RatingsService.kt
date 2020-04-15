package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.RatingsWrapper
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.LocationRating
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.CheckInRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRatingRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.LocationRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class RatingsService(
        val ratingRepository: LocationRatingRepository,
        val locationRepository: LocationRepository,
        val checkInRepository: CheckInRepository) {

    private fun getLocation(secretId: UUID): Location {
        val location = locationRepository.findBySecretId(secretId.toString())
        location.orElseThrow { GenericException("location not found") }
        return location.get()
    }

    private fun checkRatingsWrapper(ratingsWrapper: RatingsWrapper) {
        if (ratingsWrapper.rating.isPresent) {
            if (ratingsWrapper.rating.get() !in 1..5) {
                throw PropertyException(
                        field = "rating",
                        message = "A ${ratingsWrapper.rating.get()} star rating is incorrect, should be between 1 and 5.",
                        code = HttpStatus.UNPROCESSABLE_ENTITY)
            }
        } else {
            throw PropertyException("rating", "Rating should be present.", code = HttpStatus.UNPROCESSABLE_ENTITY)
        }
        if (ratingsWrapper.message.isPresent) {
            if (ratingsWrapper.message.get().length < 5){
                throw PropertyException("message", "Message should be at least 5 characters.", code = HttpStatus.UNPROCESSABLE_ENTITY)
            } else if (ratingsWrapper.message.get().length > 1024) {
                throw PropertyException("message", "Message should be at most 1024 characters.", code = HttpStatus.UNPROCESSABLE_ENTITY)
            }
        }else{
            throw PropertyException("message", "Message should be present.", code = HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

    fun addRating(creator: User, secretId: UUID, rating: RatingsWrapper): LocationRating {

        val location = getLocation(secretId)

        checkRatingsWrapper(rating)

        val optRating = ratingRepository.findByCreatorAndLocation(creator = creator, location = location)
        optRating.ifPresent { throw GenericException("You have already rated this location, update your previous rating instead.", code = HttpStatus.UNPROCESSABLE_ENTITY) }

        return ratingRepository.saveAndFlush(LocationRating(
                message = rating.message.get(),
                creator = creator,
                location = location,
                rating = rating.rating.get()
        ))
    }

    fun getRatingsByLocation(secretId: UUID): List<LocationRating> {
        return ratingRepository.findAllByLocation(getLocation(secretId))
    }

    fun getRatingById(ratingId: Int): LocationRating {
        val locationRating = ratingRepository.findById(ratingId)
        locationRating.orElseThrow { GenericException("rating not found") }
        return locationRating.get()
    }

    fun updateRatingById(ratingId: Int, ratingsWrapper: RatingsWrapper, user: User) {

        val rating = ratingRepository.findById(ratingId)
        rating.orElseThrow { GenericException("Rating not found") }

        if (rating.get().creator == user) {
            ratingsWrapper.rating.ifPresent { rating.get().rating = ratingsWrapper.rating.get() }
            ratingsWrapper.message.ifPresent { rating.get().message = ratingsWrapper.message.get() }

            ratingRepository.saveAndFlush(rating.get())
        } else {
            throw GenericException(
                    code = HttpStatus.FORBIDDEN,
                    message = "This is not your rating"
            )
        }

    }

    fun deleteRatingById(ratingId: Int, user: User) {

        val rating = ratingRepository.findById(ratingId)
        rating.orElseThrow { GenericException("Rating not found") }

        if (rating.get().creator == user) {
            ratingRepository.delete(rating.get())
        } else {
            throw GenericException(
                    code = HttpStatus.FORBIDDEN,
                    message = "This is not your rating"
            )
        }
    }
}