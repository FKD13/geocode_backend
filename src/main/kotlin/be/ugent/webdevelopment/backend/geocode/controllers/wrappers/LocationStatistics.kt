package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import com.fasterxml.jackson.annotation.JsonView

class LocationStatistics(
        val ratingsCount: Int = 0,
        val visitsCount: Int = 0,

        @JsonView(View.PrivateDetail::class)
        val lastVisit: CheckIn? = null
) : Wrapper()