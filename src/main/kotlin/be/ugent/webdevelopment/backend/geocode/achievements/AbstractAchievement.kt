package be.ugent.webdevelopment.backend.geocode.achievements

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.database.models.JsonLDSerializable
import be.ugent.webdevelopment.backend.geocode.database.models.User

abstract class AbstractAchievement() : JsonLDSerializable() {
    abstract fun achieved(user: User, achievement: Achievement): Boolean
}