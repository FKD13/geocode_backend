package be.ugent.webdevelopment.backend.geocode.jsonld.internal

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer
import com.github.jsonldjava.core.JsonLdError
import com.github.jsonldjava.core.JsonLdOptions
import com.github.jsonldjava.core.JsonLdProcessor
import java.io.IOException
import java.util.function.Supplier

/**
 * @author Alexander De Leon
 */
class JsonldBeanDeserializerModifier(private val contextSupplier: Supplier<Any>) : BeanDeserializerModifier() {
    private val mapper = ObjectMapper()
    override fun modifyDeserializer(config: DeserializationConfig, beanDesc: BeanDescription, deserializer: JsonDeserializer<*>?): JsonDeserializer<*>? {
        return if (beanDesc.classInfo.hasAnnotation(JsonldType::class.java)) {
            JsonldDelegatingDeserializer(deserializer)
        } else deserializer
    }

    internal inner class JsonldDelegatingDeserializer(delegatee: JsonDeserializer<*>?) : DelegatingDeserializer(delegatee) {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): Any {
            val input = parseJsonldObject(jp) ?: return super.deserialize(jp, ctxt)
            try {
                val options = JsonLdOptions()
                var context: Any? = contextSupplier.get()
                if (context is JsonNode) {
                    context = parseJsonldObject(initParser(mapper.treeAsTokens(context as JsonNode?)))
                }
                val obj: Any = JsonLdProcessor.compact(input, context, options)
                val newParser: JsonParser = initParser(mapper.factory.createParser(mapper.valueToTree<JsonNode>(obj).toString()))
                return super.deserialize(newParser, ctxt)
            } catch (e: JsonLdError) {
                throw JsonGenerationException("Failed to flatten json-ld", e)
            }
        }

        override fun newDelegatingInstance(newDelegatee: JsonDeserializer<*>?): JsonDeserializer<*> {
            return JsonldDelegatingDeserializer(newDelegatee)
        }

        private fun parseJsonldObject(jp: JsonParser): Any? {
            var rval: Any? = null
            val initialToken = jp.currentToken
            if (initialToken == JsonToken.START_ARRAY) {
                jp.codec = mapper
                rval = jp.readValueAs(MutableList::class.java)
            } else if (initialToken == JsonToken.START_OBJECT) {
                jp.codec = mapper
                rval = jp.readValueAs(MutableMap::class.java)
            } else if (initialToken == JsonToken.VALUE_STRING) {
                jp.codec = mapper
                rval = jp.readValueAs(String::class.java)
            } else if (initialToken == JsonToken.VALUE_FALSE || initialToken == JsonToken.VALUE_TRUE) {
                jp.codec = mapper
                rval = jp.readValueAs(Boolean::class.java)
            } else if (initialToken == JsonToken.VALUE_NUMBER_FLOAT
                    || initialToken == JsonToken.VALUE_NUMBER_INT) {
                jp.codec = mapper
                rval = jp.readValueAs(Number::class.java)
            } else if (initialToken == JsonToken.VALUE_NULL) {
                rval = null
            }
            return rval
        }
    }

    companion object {
        private fun initParser(jp: JsonParser): JsonParser {
            jp.nextToken() //put the parser at the start token
            return jp
        }
    }

}