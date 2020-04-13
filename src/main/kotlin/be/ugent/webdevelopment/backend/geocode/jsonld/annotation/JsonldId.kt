package be.ugent.webdevelopment.backend.geocode.jsonld.annotation

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside

/**
 * @author Alexander De Leon
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS)
@JacksonAnnotationsInside
annotation class JsonldId(val value: String = "")