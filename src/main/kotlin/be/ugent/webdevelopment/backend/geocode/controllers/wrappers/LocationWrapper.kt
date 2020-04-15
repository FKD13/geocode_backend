package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import java.util.*

class LocationWrapper (
        var longitude: Optional<Double>,
        var latitude: Optional<Double>,
        var secretId: Optional<String>,
        var time: Optional<Date>,
        var listed: Optional<Boolean>,
        var name: Optional<String>,
        var description: Optional<String>,
        var creatorId: Optional<Int>
        ): Wrapper(){

    constructor(loc: Location) : this(
            latitude = Optional.of(loc.latitude),
            longitude = Optional.of(loc.longitude),
            secretId = Optional.of(loc.secretId),
            time = Optional.of(loc.createdAt),
            listed = Optional.of(loc.listed),
            name = Optional.of(loc.name),
            description = Optional.of(loc.description),
            creatorId = Optional.of(loc.creator.id)
            )
}
