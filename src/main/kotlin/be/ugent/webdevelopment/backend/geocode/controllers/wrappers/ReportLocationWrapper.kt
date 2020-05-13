package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.Location

class ReportLocationWrapper(
        val location: Location,
        val reportsCount: Int
) : Wrapper()