package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView

class ExtendedUserWrapper (

        @field:JsonUnwrapped
        var user: User,

        @field:JsonView(View.List::class)
        @field:JsonldProperty("https://schema.org/image")
        var avatarUrl: String

): Wrapper()