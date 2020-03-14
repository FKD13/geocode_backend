package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.ExceptionContainerWrapper
import java.util.*

class ExceptionContainer(code: Int = 404,
                         inputErrors: LinkedList<PropertyException> = LinkedList(),
                         generalExceptions: LinkedList<GenericException> = LinkedList()) : GeocodeException(code) {

    var inputExceptions: LinkedList<PropertyException> = inputErrors
        private set

    var generalExceptions: LinkedList<GenericException> = generalExceptions
        private set

    fun addException(exception: GenericException) {
        generalExceptions.add(exception)
    }

    fun addException(exception: PropertyException) {
        inputExceptions.add(exception)
    }

    override fun wrap(): ExceptionContainerWrapper = ExceptionContainerWrapper(this)
}