package be.ugent.webdevelopment.backend.geocode.controllers.wrappers

import java.util.*

class RatingsWrapper(
        val rating: Optional<Int>,
        val message: Optional<String>
): Wrapper