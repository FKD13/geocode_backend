package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView
import java.util.*

class UserAchievementWrapper(
        @field:JsonUnwrapped
        val achievement: Achievement,

        @field:JsonView(View.List::class)
        @field:JsonldProperty("https://schema.org/CreativeWork#dateCreated")
        val achievedAt: Date
) : Wrapper() {
}