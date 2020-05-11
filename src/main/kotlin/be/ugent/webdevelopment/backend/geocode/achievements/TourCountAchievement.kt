package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.User

class TourCountAchievement: AbstractAchievement() {
    override fun achieved(user: User, achievement: Achievement): Boolean {
        TODO("Not yet implemented")
    }
}