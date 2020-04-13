package be.ugent.webdevelopment.backend.geocode.jsonld.annotation

import java.lang.annotation.Inherited

/**
 * @author Alexander De Leon
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Inherited
@Repeatable
annotation class JsonldLink(val rel: String, val name: String, val href: String)