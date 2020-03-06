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

    private val oauthParty = OauthParty(oauthPartyName = "Google")
    private val user = User(oauthId = "1234", oauthParty = oauthParty, username = "Jhon", email = "john@mail.com")
    private val location = Location(longitude = 2.0, latitude = 4.0, listed = false, name = "Epic Location",
            description = "...", creator = user)

    @Test
    fun oauthPartyRepositoryTest() {
        oauthPartyRepository.save(oauthParty)
        val found = oauthPartyRepository.findByOauthPartyName(oauthParty.oauthPartyName)
        assertThat(found).isNotNull
        assertThat(found).isEqualTo(oauthParty)
    }

    @Test
    fun userRepositoryTest() {
        userRepository.save(user)
        val found: Optional<User> = userRepository.findById(user.id)
        assertThat(found).isNotNull
        assertThat(userRepository.findByUsername("fred").size).isEqualTo(0)
        assertThat(userRepository.findByUsername(user.username).size).isEqualTo(1)
        assertThat(found.get().oauthParty).isEqualTo(oauthParty)
    }

    @Test
    fun locationRepositoryTest() {
        locationRepository.save(location)
        val found = locationRepository.findByCreator(user)
        assertThat(found.size).isEqualTo(1)
    }
}