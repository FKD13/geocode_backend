package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldResource
import be.ugent.webdevelopment.backend.geocode.jsonld.util.AnnotationsUtils
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase

/**
 * @author Alexander De Leon
 */
class JsonldResourceSerializerModifier : BeanSerializerModifier() {
    override fun modifySerializer(config: SerializationConfig, beanDesc: BeanDescription, serializer: JsonSerializer<*>): JsonSerializer<*> {
        return if (AnnotationsUtils.isAnnotationPresent(beanDesc.beanClass, JsonldResource::class.java) && serializer is BeanSerializerBase) {
            JsonldResourceSerializer(serializer)
        } else serializer
    }
}