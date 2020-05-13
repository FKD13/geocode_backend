package be.ugent.webdevelopment.backend.geocode.exceptions.wrappers

import be.ugent.webdevelopment.backend.geocode.database.models.JsonLDSerializable
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType

@JsonldType("https://schema.org/Action")
class ExceptionContainerWrapper(ec: ExceptionContainer) : Wrapper {

    @field:JsonldProperty("https://schema.org/Action#error")
    val inputErrors: List<PropertyExceptionWrapper> = ec.inputExceptions.map { it.wrap() }

    @field:JsonldProperty("https://schema.org/Action#error")
    val generalErrors: List<GenericExceptionWrapper> = ec.generalExceptions.map { it.wrap() }
}

@JsonldType("https://schema.org/Action")
class GenericExceptionWrapper(ge: GenericException) : Wrapper {

    @field:JsonldProperty("https://schema.org/Action#error")
    val message: String = ge.message
}

@JsonldType("https://schema.org/Action")
class PropertyExceptionWrapper(pe: PropertyException) : Wrapper {

    @field:JsonldProperty("https://schema.org/Action#name")
    val field: String = pe.field

    @field:JsonldProperty("https://schema.org/Action#error")
    val message: String = pe.message
}

interface Wrapper : JsonLDSerializable
