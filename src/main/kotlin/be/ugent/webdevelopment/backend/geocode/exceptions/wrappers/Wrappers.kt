package be.ugent.webdevelopment.backend.geocode.exceptions.wrappers

import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException

class ExceptionContainerWrapper(ec: ExceptionContainer) : Wrapper {
    val inputErrors: List<PropertyExceptionWrapper> = ec.inputExceptions.map { it.wrap() }
    val generalErrors: List<GenericExceptionWrapper> = ec.generalExceptions.map { it.wrap() }
}

class GenericExceptionWrapper(ge: GenericException) : Wrapper {
    val message: String = ge.message
}

class PropertyExceptionWrapper(pe: PropertyException) : Wrapper {
    val field: String = pe.field
    val message: String = pe.message
}

interface Wrapper