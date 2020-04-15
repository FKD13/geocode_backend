package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.ExceptionContainerWrapper
import org.springframework.http.HttpStatus
import java.util.*

class ExceptionContainer(code: HttpStatus = HttpStatus.NOT_FOUND,
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

    fun isEmpty(): Boolean{
        return inputExceptions.isEmpty() && generalExceptions.isEmpty()
    }

    fun ifEmpty(f: () -> Unit) {
        if (isEmpty()) {
           f.invoke()
        }
    }

    fun ifNotEmpty(f: () -> Unit) {
        if (isEmpty().not()) {
            f.invoke()
        }
    }

    fun ifEmptyOrElse(f1: () -> Unit, f2: () -> Unit) {
        if (isEmpty()) {
            f1.invoke()
        } else {
            f2.invoke()
        }
    }

    fun throwIfNotEmpty(){
        if (!isEmpty()){
            throw this
        }
    }

    override fun wrap(): ExceptionContainerWrapper = ExceptionContainerWrapper(this)
}