package be.ugent.webdevelopment.backend.geocode.jsonld

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils.getFullIdFromObject
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import java.util.*

/**
 * @author Alexander De Leon
 * Edited by:
 * @author Arthur Deruytter
 */
object JsonldContextFactory {

    fun fromAnnotations(objType: Any, provider: SerializerProvider): Optional<ObjectNode> {
        val generatedContext = JsonNodeFactory.withExactBigDecimals(true).objectNode()

        val fieldContexts = generateContextsForFields(objType, provider)
        fieldContexts.forEach { (fieldName: String?, value: JsonNode?) -> generatedContext.set<JsonNode>(fieldName, value) }

        //Return absent optional if context is empty
        return if (generatedContext.size() != 0) Optional.of(generatedContext) else Optional.empty()
    }

    private fun generateContextsForFields(objType: Any, provider: SerializerProvider): Map<String, JsonNode> {
        val contexts: MutableMap<String, JsonNode> = HashMap()
        var currentClass: Class<*> = objType.javaClass

        while (currentClass != Any::class.java) {
            val fields = currentClass.declaredFields
            for (f in fields) {
                f.isAccessible = true
                if ((f.isAnnotationPresent(JsonldId::class.java) || f.name == "this$0") ||
                        (provider.activeView != null && (
                                f.isAnnotationPresent(JsonView::class.java) &&
                                        f.getAnnotation(JsonView::class.java).value.all {
                                            !it.java.isAssignableFrom(provider.activeView)
                                        }))) {
                    continue
                }
                if (f.isAnnotationPresent(JsonUnwrapped::class.java)) {
                    generateContextsForFields(f.get(objType), provider).forEach { (fieldName, value) ->
                        if (!contexts.containsKey(fieldName)) {
                            contexts[fieldName] = value
                        }
                    }
                    continue
                }
                val jsonldProperty = f.getAnnotation(JsonldProperty::class.java)
                var propertyId: Optional<String> = Optional.empty()
                if (jsonldProperty != null) {
                    propertyId = Optional.of(jsonldProperty.value)
                }
                propertyId.ifPresent { id: String? ->
                    if (f.type.declaredFields.any { df -> df.isAnnotationPresent(JsonldId::class.java) }) {
                        val idfield = f.type.declaredFields.filter { df -> df.isAnnotationPresent(JsonldId::class.java) }[0]//dit gaat altijd maar 1 element hebben
                        idfield.isAccessible = true
                        val node = JsonNodeFactory.withExactBigDecimals(true).objectNode()
                        getFullIdFromObject(f.get(objType)).ifPresent {
                            node.set<JsonNode>("@id", TextNode.valueOf(it))
                        }
                        node.set<JsonNode>("@type", TextNode.valueOf(id))
                        contexts[f.name] = node
                    } else {
                        if (f.isAnnotationPresent(JsonProperty::class.java)) {
                            contexts[f.getAnnotation(JsonProperty::class.java).value] = TextNode.valueOf(id)
                        } else {
                            contexts[f.name] = TextNode.valueOf(id)
                        }
                    }
                }
            }
            currentClass = currentClass.superclass
        }
        return contexts
    }
}