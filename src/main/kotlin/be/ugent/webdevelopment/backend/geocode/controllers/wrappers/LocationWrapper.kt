package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.Location
import java.time.LocalDateTime

class LocationWrapper (
        longitude: Double?,
        latitude: Double?,
        secretId: String?,
        time: LocalDateTime?,
        listed: Boolean?,
        name: String?,
        description: String?
        ): Wrapper{

    constructor(loc: Location) : this(
            latitude = loc.latitude,
            longitude = loc.longitude,
            secretId = loc.secretId,
            time = loc.time,
            listed = loc.listed,
            name = loc.name,
            description = loc.description
            )
}
