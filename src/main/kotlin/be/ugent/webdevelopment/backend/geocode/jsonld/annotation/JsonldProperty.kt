package be.ugent.webdevelopment.backend.geocode.jsonld.annotation

import java.lang.annotation.Inherited

/**
 * @author Alexander De Leon
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CONSTRUCTOR,
AnnotationTarget.CLASS, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Inherited
annotation class JsonldProperty(val value: String)