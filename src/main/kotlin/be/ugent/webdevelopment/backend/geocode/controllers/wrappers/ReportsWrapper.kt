package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class ReportsWrapper(
        var imageId: Optional<Int>,
        var reason: Optional<String>,
        var resolved: Optional<Boolean>
) : Wrapper()