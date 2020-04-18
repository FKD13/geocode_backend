package be.ugent.webdevelopment.backend.geocode.jsonld.util

import be.ugent.webdevelopment.backend.geocode.jsonld.JsonldContextFactory.fromAnnotations
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldTypeFromJavaClass
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.AnnotationConstants
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.ObjectNode
import java.util.*

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
object JsonldResourceUtils {

    fun getContext(scopedObj: Any, provider: SerializerProvider): Optional<ObjectNode> {
        return fromAnnotations(scopedObj, provider)
    }

    fun dynamicTypeLookup(objType: Class<*>): Optional<String> {
        val typeFromAnnotation = Optional.ofNullable(objType.getAnnotation(JsonldType::class.java))
                .map { obj: JsonldType -> obj.value }
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

    private fun getIdValueFromClassAnnotation(obj: Any): Optional<String> {
        val annotation = obj.javaClass.getAnnotation(JsonldId::class.java)
        return if (annotation == null) {
            Optional.empty()
        } else {
            Optional.of(annotation.value)
        }
    }

    private fun appendIfNeeded(string: String, append: String = "/"): String {
        return if (string.endsWith(append)) {
            string
        } else {
            "$string$append"
        }
    }

    fun getFullIdFromObject(obj: Any): Optional<String> {
        val id = getIdValueFromObject(obj)
        val idValue = getIdValueFromClassAnnotation(obj)

        return if (id.isPresent && idValue.isPresent) {
            Optional.of(appendIfNeeded(System.getenv("GEOCODE_BACKEND_URL")) + appendIfNeeded(idValue.get()) + id.get())
        } else {
            Optional.empty()
        }
    }

    private fun getIdValueFromObject(obj: Any): Optional<Any> {
        val listOfFields = obj.javaClass.declaredFields.filter { field -> field.isAnnotationPresent(JsonldId::class.java) }
        return when {
            listOfFields.isEmpty() -> {
                Optional.empty()
            }
            listOfFields.size > 1 -> {
                Optional.empty() //this is ambiguous
            }
            else -> {
                val field = listOfFields[0]
                field.isAccessible = true
                Optional.of(field.get(obj).toString())
            }
        }
    }
}