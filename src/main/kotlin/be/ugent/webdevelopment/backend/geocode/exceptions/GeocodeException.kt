package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.Wrapper

abstract class GeocodeException(val code: Int) : Throwable() {
    abstract fun wrap() : Wrapper
}