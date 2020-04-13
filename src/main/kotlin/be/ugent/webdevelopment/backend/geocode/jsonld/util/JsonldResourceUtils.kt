package be.ugent.webdevelopment.backend.geocode.jsonld.util

import be.ugent.webdevelopment.backend.geocode.jsonld.JsonldContextFactory.fromAnnotations
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldTypeFromJavaClass
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.AnnotationConstants
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*
import java.util.function.Function

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
object JsonldResourceUtils {
    fun getContext(scopedObj: Any, provider: SerializerProvider?): Optional<ObjectNode> {
        return fromAnnotations(scopedObj.javaClass, provider)
    }

    fun dynamicTypeLookup(objType: Class<*>): Optional<String> {
        val typeFromAnnotation = Optional.ofNullable(objType.getAnnotation(JsonldType::class.java))
                .map(Function<JsonldType, String> { obj: JsonldType -> obj.value })
        return if (typeFromAnnotation.isPresent) typeFromAnnotation else typeFromJavaClass(objType)
    }

    private fun typeFromJavaClass(objType: Class<*>): Optional<String> {
        return Optional.ofNullable(objType.getAnnotation(JsonldTypeFromJavaClass::class.java))
                .map { t: JsonldTypeFromJavaClass ->
                    var prefix: String = t.namespace
                    if (prefix == AnnotationConstants.UNASSIGNED) {
                        prefix = if (t.namespacePrefix == AnnotationConstants.UNASSIGNED) "" else t.namespacePrefix + ":"
                    }
                    prefix + objType.simpleName
                }
    }
}