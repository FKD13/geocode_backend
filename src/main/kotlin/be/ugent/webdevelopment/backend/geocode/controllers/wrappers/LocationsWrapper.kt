package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.Location

class LocationsWrapper(var longitude: Double?,
                       var latitude: Double?,
                       var name: String?,
                       var description: String?,
                       var creatorId: Int?,
                       var secretId: String?,
                       var listed: Boolean?
) : Wrapper {
constructor(loc: Location) : this(longitude = loc.longitude,
        latitude = loc.latitude,
        name = loc.name,
        description = loc.description,
        creatorId = loc.creator.id,
        secretId = loc.secretId,
        listed = loc.listed
)

}
