package be.ugent.webdevelopment.backend.geocode.database

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.database.models.OauthParty
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class RepositoryTest @Autowired constructor (
        val userTourRepository: UserTourRepository,
        val userRepository: UserRepository,
        val tourRepository: TourRepository,
        val reportRepository: ReportRepository,
        val oauthPartyRepository: OauthPartyRepository,
        val locationRepository: LocationRepository,
        val locationRatingRepository: LocationRatingRepository,
        val commentRepository: CommentRepository,
        val checkInRepository: CheckInRepository) {
}