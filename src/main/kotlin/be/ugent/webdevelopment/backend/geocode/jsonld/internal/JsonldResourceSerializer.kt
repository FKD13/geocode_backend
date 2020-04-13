package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldLink
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanSerializer
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase
import java.io.IOException
import java.util.*

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
class JsonldResourceSerializer(src: BeanSerializerBase?) : BeanSerializer(src) {

    @Throws(IOException::class, JsonGenerationException::class)
    override fun serializeFields(bean: Any, jgen: JsonGenerator, provider: SerializerProvider) {
        val type = JsonldResourceUtils.dynamicTypeLookup(bean.javaClass)
        if (type.isPresent) {
            jgen.writeStringField("@type", type.get())
        }

        val id = JsonldResourceUtils.getFullIdFromObject(bean)
        id.ifPresent { jgen.writeStringField("@id", id.get()) }

        val context = JsonldResourceUtils.getContext(bean, provider)
        if (context.isPresent) {
            jgen.writeObjectField("@context", context.get())
        }
        super.serializeFields(bean, jgen, provider)//Maybe todo en zelf serializen als JsonViews niet werken
        getLinks(bean).ifPresent { linksMap: Map<String?, String?>? ->
            linksMap!!.forEach { (key: String?, value: String?) ->
                try {
                    jgen.writeStringField(key, value)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }

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
}