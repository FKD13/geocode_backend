package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView

class ExtendedLocationWrapper constructor(

        @field:JsonUnwrapped
        var loc: Location,

        @field:JsonView(View.PublicDetail::class)
        @field:JsonldProperty("https://schema.org/Rating#ratingValue")
        var rating: Double
) : Wrapper()