package be.ugent.webdevelopment.backend.geocode.database

import be.ugent.webdevelopment.backend.geocode.database.models.OauthParty
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.database.repositories.*
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.logging.LogLevel
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.Query
import java.util.logging.LogManager
import java.util.logging.Logger


@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //https://stackoverflow.com/questions/41315386/spring-boot-1-4-datajpatest-error-creating-bean-with-name-datasource
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

    val oauthParty = OauthParty(oauthPartyName = "Google")
    val user = User(oauthId = "1234", oauthParty = oauthParty, username = "Jhon", email = "john@mail.com")

    @Test
    fun oauthPartyRepositoryTest() {
        oauthPartyRepository.save(oauthParty)
        oauthPartyRepository.findAll().forEach{ it -> println("${it.id} - ${it.oauthPartyName}") }
    }
}