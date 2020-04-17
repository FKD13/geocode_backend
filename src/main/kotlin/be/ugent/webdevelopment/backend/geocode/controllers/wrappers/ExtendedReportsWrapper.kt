package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Report
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView

class ExtendedReportsWrapper(

        @JsonUnwrapped
        var report: Report,

        @field:JsonldProperty("https://schema.org/Review#image")
        @field:JsonView(View.AdminDetail::class)
        var imageUrl: String
) : Wrapper()