package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class TourWrapper(
        var name: Optional<String>,
        var description: Optional<String>,
        var locations: Optional<List<String>>
)