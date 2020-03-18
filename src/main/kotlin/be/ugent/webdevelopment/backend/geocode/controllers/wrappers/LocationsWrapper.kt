package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import java.util.*

class LocationsWrapper(var longitude: Optional<Double>,
                       var latitude: Optional<Double>,
                       var name: Optional<String>,
                       var description: Optional<String>,
                       var creatorId: Optional<Int>,
                       var secretId: Optional<String>,
                       var listed: Optional<Boolean>
) : Wrapper {
    constructor(loc: Location) : this(
            longitude = Optional.of(loc.longitude),
            latitude = Optional.of(loc.latitude),
            name = Optional.of(loc.name),
            description = Optional.of(loc.description),
            creatorId = Optional.of(loc.creator.id),
            secretId = Optional.of(loc.secretId),
            listed = Optional.of(loc.listed)
    )

}
