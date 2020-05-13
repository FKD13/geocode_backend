package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class LocationWrapper(
        var longitude: Optional<Double>,
        var latitude: Optional<Double>,
        var secretId: Optional<String>,
        var listed: Optional<Boolean>,
        var name: Optional<String>,
        var description: Optional<String>,
        var creatorId: Optional<Int>,
        var active: Optional<Boolean>
) : Wrapper()
