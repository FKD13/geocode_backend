package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class DeleteWrappper(
        var type: Optional<DATATYPE>
) : Wrapper()

enum class DATATYPE {
    COMMENTS,
    RATINGS,
    LOCATIONS,
    TOURS,
    VISITS
}
