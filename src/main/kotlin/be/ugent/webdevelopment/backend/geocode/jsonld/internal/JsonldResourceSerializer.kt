package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.database.models.JsonLDSerializable
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */

class JsonldResourceSerializer : StdSerializer<JsonLDSerializable>(JsonLDSerializable::class.java) {

    override fun handledType(): Class<JsonLDSerializable> {
        return JsonLDSerializable::class.java
    }

    override fun serialize(value: JsonLDSerializable, gen: JsonGenerator, provider: SerializerProvider) {

        val type = JsonldResourceUtils.dynamicTypeLookup(value.javaClass)
        val id = JsonldResourceUtils.getFullIdFromObject(value)
        val context = JsonldResourceUtils.getContext(value, provider)

        gen.writeStartObject()

        type.ifPresent { gen.writeStringField("@type", type.get()) }
        id.ifPresent { gen.writeStringField("@id", id.get()) }
        context.ifPresent { gen.writeObjectField("@context", context.get()) }

        value.javaClass.declaredFields.filter { !it.isAnnotationPresent(JsonIgnore::class.java) }
                .filter {
                    !it.isAnnotationPresent(JsonView::class.java) ||
                            it.getAnnotation(JsonView::class.java).value.any {
                                it.java.isAssignableFrom(provider.activeView)
                            }
                }
                .forEach {
                    it.isAccessible = true
                    if (it.isAnnotationPresent(JsonUnwrapped::class.java)) {
                        serializeUnwrapped(it.get(value) as JsonLDSerializable, gen, provider)
                    } else {
                        provider.defaultSerializeField(it.name, it.get(value), gen)
                    }
                }
        gen.writeEndObject()
    }

    fun serializeUnwrapped(value: JsonLDSerializable, gen: JsonGenerator, provider: SerializerProvider) {
        /* Als je hem unwrapt dan moet je geen context meer genereren.
        Dit wordt hierboven gedaan

        val context = JsonldResourceUtils.getContext(value, provider)
        context.ifPresent { gen.writeObjectField("@context", context.get()) }
        */

        val type = JsonldResourceUtils.dynamicTypeLookup(value.javaClass)
        val id = JsonldResourceUtils.getFullIdFromObject(value)
        type.ifPresent { gen.writeStringField("@type", type.get()) }
        id.ifPresent { gen.writeStringField("@id", id.get()) }



        value.javaClass.declaredFields.filter { !it.isAnnotationPresent(JsonIgnore::class.java) }
                .filter {
                    !it.isAnnotationPresent(JsonView::class.java) ||
                            it.getAnnotation(JsonView::class.java).value.any {
                                it.java.isAssignableFrom(provider.activeView)
                            }
                }
                .forEach {
                    it.isAccessible = true
                    if (it.isAnnotationPresent(JsonUnwrapped::class.java)) {
                        serializeUnwrapped(value, gen, provider)
                    } else {
                        provider.defaultSerializeField(it.name, it.get(value), gen)
                    }
                    //body.set<JsonNode>(it.name, TextNode.valueOf(it.get(value).toString()))
                }
    }


}