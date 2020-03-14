package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.GenericExceptionWrapper

class GenericException(val error: String) : GeocodeException, Throwable() {
    override fun wrap(): GenericExceptionWrapper = GenericExceptionWrapper(this)
}
