package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.GenericExceptionWrapper

class GenericException(override val message: String, code: Int = 404 ) : GeocodeException(code) {
    override fun wrap(): GenericExceptionWrapper = GenericExceptionWrapper(this)
}
