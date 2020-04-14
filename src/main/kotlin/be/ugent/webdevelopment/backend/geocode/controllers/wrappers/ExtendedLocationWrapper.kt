package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView

class ExtendedLocationWrapper constructor(

        @field:JsonUnwrapped
        var loc: Location,

        @field:JsonView(View.PublicDetail::class)
        var rating: Double
) : Wrapper()