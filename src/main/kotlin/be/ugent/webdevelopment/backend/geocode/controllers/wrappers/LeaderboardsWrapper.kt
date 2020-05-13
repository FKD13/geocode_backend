package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.User
import com.fasterxml.jackson.annotation.JsonView

class LeaderboardsWrapper(
        val user: User,

        @field:JsonView(View.List::class)
        val value: Int
) : Wrapper()