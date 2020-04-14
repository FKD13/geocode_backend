package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.database.models.Model
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldLink
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.util.*

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */

class JsonldResourceSerializer : StdSerializer<Model>(Model::class.java) {

    protected fun getLinks(resource: Model): Optional<Map<String?, String?>> {
        var linksNodes: MutableMap<String?, String?>? = null
        val beanType: Class<*> = resource.javaClass
        val links = beanType.getAnnotationsByType(JsonldLink::class.java)
        linksNodes = HashMap(links.size)
        for (i in links.indices) {
            linksNodes[links[i].name] = links[i].href
        }
        return Optional.ofNullable(linksNodes)
    }

    override fun handledType(): Class<Model> {
        return Model::class.java
    }

    override fun serialize(value: Model, jgen: JsonGenerator, serializers: SerializerProvider) {
        val type = JsonldResourceUtils.dynamicTypeLookup(value.javaClass)
        jgen.writeStartObject()
        if (type.isPresent) {
            jgen.writeStringField("@type", type.get())
        }

        val id = JsonldResourceUtils.getFullIdFromObject(value)
        id.ifPresent { jgen.writeStringField("@id", id.get()) }

        val context = JsonldResourceUtils.getContext(value, serializers)
        System.out.println("CONTEXT= $context")

        if (context.isPresent) {
            jgen.writeObjectField("@context", context.get())
        }

        System.out.println("VIEW= ${serializers.activeView}")
        value.javaClass.declaredFields.filter { !it.isAnnotationPresent(JsonIgnore::class.java) }
                .filter { !it.isAnnotationPresent(JsonView::class.java) || it.getAnnotation(JsonView::class.java).value.any { it == serializers.activeView.kotlin } }
                .forEach {
                    it.isAccessible = true
                    serializers.defaultSerializeField(it.name, it.get(value), jgen)
                }


        getLinks(value).ifPresent { linksMap: Map<String?, String?>? ->
            linksMap!!.forEach { (key: String?, value: String?) ->
                try {
                    jgen.writeStringField(key, value)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
        jgen.writeEndObject()
    }
}