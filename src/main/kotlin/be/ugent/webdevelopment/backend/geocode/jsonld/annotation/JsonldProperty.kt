package be.ugent.webdevelopment.backend.geocode.jsonld.annotation

import java.lang.annotation.Inherited

/**
 * @author Alexander De Leon
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER)
@Inherited
annotation class JsonldProperty(val value: String)