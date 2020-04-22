package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.Wrapper
import org.springframework.http.HttpStatus

abstract class GeocodeException(val code: HttpStatus) : Throwable() {
    abstract fun wrap(): Wrapper
}