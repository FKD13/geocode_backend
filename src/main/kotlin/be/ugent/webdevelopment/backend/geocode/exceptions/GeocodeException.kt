package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.Wrapper

interface GeocodeException {
    fun wrap() : Wrapper
}