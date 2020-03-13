package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.PropertyExceptionWrapper

class PropertyException(val field: String, override val message: String) : GeocodeException, Throwable() {
    override fun wrap(): PropertyExceptionWrapper = PropertyExceptionWrapper(this)
}