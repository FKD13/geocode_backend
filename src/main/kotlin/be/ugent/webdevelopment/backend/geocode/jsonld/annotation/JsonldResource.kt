package be.ugent.webdevelopment.backend.geocode.jsonld.annotation

import java.lang.annotation.Inherited

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Inherited
annotation class JsonldResource