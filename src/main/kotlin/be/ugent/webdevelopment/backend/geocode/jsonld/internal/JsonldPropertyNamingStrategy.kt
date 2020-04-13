package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedField
import com.fasterxml.jackson.databind.introspect.AnnotatedMember
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter
import java.util.*

/**
 * @author Alexander De Leon
 */
class JsonldPropertyNamingStrategy : PropertyNamingStrategy() {
    override fun nameForField(config: MapperConfig<*>?, field: AnnotatedField, defaultName: String): String {
        val name = if (config is DeserializationConfig) jsonldName(field) else null
        return Optional.ofNullable(name).orElse(super.nameForField(config, field, defaultName))
    }

    override fun nameForGetterMethod(config: MapperConfig<*>?, method: AnnotatedMethod, defaultName: String): String {
        val name = if (config is DeserializationConfig) jsonldName(method) else null
        return Optional.ofNullable(name).orElse(super.nameForGetterMethod(config, method, defaultName))
    }

    override fun nameForSetterMethod(config: MapperConfig<*>?, method: AnnotatedMethod, defaultName: String): String {
        val name = if (config is DeserializationConfig) jsonldName(method) else null
        return Optional.ofNullable(name).orElse(super.nameForSetterMethod(config, method, defaultName))
    }

    override fun nameForConstructorParameter(config: MapperConfig<*>?, ctorParam: AnnotatedParameter, defaultName: String): String {
        val name = if (config is DeserializationConfig) jsonldName(ctorParam) else null
        return Optional.ofNullable(name).orElse(super.nameForConstructorParameter(config, ctorParam, defaultName))
    }

    private fun jsonldName(member: AnnotatedMember): String? {
        val jsonldProperty = member.getAnnotation(JsonldProperty::class.java)
        return jsonldProperty?.value
    }
}