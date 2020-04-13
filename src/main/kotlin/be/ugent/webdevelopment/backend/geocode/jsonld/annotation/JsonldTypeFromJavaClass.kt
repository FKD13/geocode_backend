package be.ugent.webdevelopment.backend.geocode.jsonld.annotation

import be.ugent.webdevelopment.backend.geocode.jsonld.internal.AnnotationConstants
import java.lang.annotation.Inherited

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Inherited
@JsonldResource
annotation class JsonldTypeFromJavaClass(val namespace: String = AnnotationConstants.UNASSIGNED, val namespacePrefix: String = AnnotationConstants.UNASSIGNED)