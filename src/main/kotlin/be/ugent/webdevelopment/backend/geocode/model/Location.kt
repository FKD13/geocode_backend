package be.ugent.webdevelopment.backend.geocode.model

import java.sql.Timestamp
import java.util.*

class Location(var creator_id: Long?, var long: Long?, var lat: Long?, var secret_id: UUID?, var time: Timestamp?,
               var listed: Boolean?, var name: String?, var description: String?){

    constructor(secret_id: UUID, location: Location) : this(location.creator_id, location.long, location.lat,
            secret_id, location.time, location.listed, location.name, location.description)
}