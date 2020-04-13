package be.ugent.webdevelopment.backend.geocode.jsonld.util

import java.util.*

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
object AnnotationsUtils {
    fun isAnnotationPresent(type: Class<*>, annotationType: Class<out Annotation>): Boolean {
        return isAnnotationPresent(type, annotationType, ArrayList())
    }

    internal fun isAnnotationPresent(type: Class<*>, annotationType: Class<out Annotation>, ignore: MutableList<Class<*>?>): Boolean {
        if (type.isAnnotationPresent(annotationType)) {
            return true
        }
        if (type.annotations.isEmpty()) {
            return false
        }
        for (a: Annotation in type.annotations) {
            if (!ignore.contains(a.annotationClass.javaObjectType)) {
                ignore.add(type)
                if (isAnnotationPresent(a.annotationClass.javaObjectType, annotationType, ignore)) {
                    return true
                }
            }
        }
        return false
    }
}