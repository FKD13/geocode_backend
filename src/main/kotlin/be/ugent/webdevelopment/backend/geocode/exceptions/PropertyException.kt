package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.PropertyExceptionWrapper
import org.springframework.http.HttpStatus

class PropertyException(val field: String, override val message: String,
                        code: HttpStatus = HttpStatus.EXPECTATION_FAILED) : GeocodeException(code) {
    override fun wrap(): PropertyExceptionWrapper = PropertyExceptionWrapper(this)
}