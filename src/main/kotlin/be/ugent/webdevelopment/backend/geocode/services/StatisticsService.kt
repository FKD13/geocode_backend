package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.GeneralStatistics
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserStatistics
import be.ugent.webdevelopment.backend.geocode.database.models.User
import org.springframework.stereotype.Service

@Service
class StatisticsService {

    fun getUserStatistics(user: User): UserStatistics {
        return UserStatistics(0, 0, 0)
    }

    fun getStatistics(): GeneralStatistics {
        return GeneralStatistics(0, 0, 0, 0)
    }

}