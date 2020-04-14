package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldLink
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.util.*

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */

class JsonldResourceSerializer : JsonSerializer<Any>() {

    protected fun getLinks(resource: Any): Optional<Map<String?, String?>> {
        var linksNodes: MutableMap<String?, String?>? = null
        val beanType: Class<*> = resource.javaClass
        val links = beanType.getAnnotationsByType(JsonldLink::class.java)
        linksNodes = HashMap(links.size)
        for (i in links.indices) {
            linksNodes[links[i].name] = links[i].href
        }
        return Optional.ofNullable(linksNodes)
    }

    @Throws(IOException::class, JsonGenerationException::class)
    override fun serialize(value: Any, jgen: JsonGenerator, serializers: SerializerProvider) {
        val type = JsonldResourceUtils.dynamicTypeLookup(value.javaClass)
        if (type.isPresent) {
            jgen.writeStringField("@type", type.get())
        }

        val id = JsonldResourceUtils.getFullIdFromObject(value)
        id.ifPresent { jgen.writeStringField("@id", id.get()) }

        val context = JsonldResourceUtils.getContext(value, serializers)
        if (context.isPresent) {
            jgen.writeObjectField("@context", context.get())
        }

        serializers.defaultSerializeValue(value, jgen)

        getLinks(value).ifPresent { linksMap: Map<String?, String?>? ->
            linksMap!!.forEach { (key: String?, value: String?) ->
                try {
                    jgen.writeStringField(key, value)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}