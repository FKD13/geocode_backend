package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.GenericExceptionWrapper
import org.springframework.http.HttpStatus

class GenericException(override val message: String, code: HttpStatus = HttpStatus.NOT_FOUND) : GeocodeException(code) {
    override fun wrap(): GenericExceptionWrapper = GenericExceptionWrapper(this)
}
