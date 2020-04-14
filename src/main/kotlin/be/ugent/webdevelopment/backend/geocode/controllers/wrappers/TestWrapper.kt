package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.JsonldResourceSerializer
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonSerialize

class TestWrapper constructor(

        @field:JsonUnwrapped
        @field:JsonView(View.PublicDetail::class)
        var loc: Location = Location(),

        @field:JsonView(View.PublicDetail::class)
        var rating : Int = 5
) : Wrapper(){


}