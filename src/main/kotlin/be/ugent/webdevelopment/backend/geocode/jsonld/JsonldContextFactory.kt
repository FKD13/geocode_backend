package be.ugent.webdevelopment.backend.geocode.jsonld

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldLink
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldNamespace
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils.getFullIdFromObject
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import java.util.*
import java.util.function.Consumer

/**
 * @author Alexander De Leon
 * Edited by:
 * @author Arthur Deruytter
 */
object JsonldContextFactory {

    fun fromAnnotations(objType: Any, provider: SerializerProvider): Optional<ObjectNode> {
        val generatedContext = JsonNodeFactory.withExactBigDecimals(true).objectNode()
        generateNamespaces(objType.javaClass).forEach { (name: String?, uri: String?) -> generatedContext.set<JsonNode>(name, TextNode(uri)) }
        //TODO: This is bad...it does not consider other Jackson annotations. Need to use a AnnotationIntrospector?
        val fieldContexts = generateContextsForFields(objType, provider)
        fieldContexts.forEach { (fieldName: String?, value: JsonNode?) -> generatedContext.set<JsonNode>(fieldName, value) }
        //add links
        val links = objType.javaClass.getAnnotationsByType(JsonldLink::class.java)
        for (link in links) {
            val linkNode = JsonNodeFactory.withExactBigDecimals(true)
                    .objectNode()
            linkNode.set<JsonNode>("@id", TextNode(link.rel))
            linkNode.set<JsonNode>("@type", TextNode("@id"))
            generatedContext.set<JsonNode>(link.name, linkNode)
        }
        //Return absent optional if context is empty
        return if (generatedContext.size() != 0) Optional.of(generatedContext) else Optional.empty()
    }

    private fun generateContextsForFields(objType: Any, provider: SerializerProvider): Map<String, JsonNode> {
        return generateContextsForFields(objType, ArrayList(), provider)
    }

    private fun generateContextsForFields(objType: Any, ignoreTypes: List<Class<*>>, provider: SerializerProvider): Map<String, JsonNode> {
        val contexts: MutableMap<String, JsonNode> = HashMap()
        var currentClass: Class<*> = objType.javaClass
        var namespace: Optional<JsonldNamespace> = Optional.ofNullable(currentClass.getAnnotation(JsonldNamespace::class.java))

        while (currentClass != Any::class.java) {
            val fields = currentClass.declaredFields
            for (f in fields) {
                if (f.isAnnotationPresent(JsonldId::class.java) || f.name == "this$0") {
                    continue
                }
                if (f.isAnnotationPresent(JsonView::class.java) && !f.getAnnotation(JsonView::class.java).value.any { it == provider.activeView.kotlin }) {
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
                        f.isAccessible = true
                        val node = JsonNodeFactory.withExactBigDecimals(true).objectNode()
                        getFullIdFromObject(f.get(objType)).ifPresent {
                            node.set<JsonNode>("@id", TextNode.valueOf(it))
                        }
                        node.set<JsonNode>("@type", TextNode.valueOf(id))
                        contexts[f.name] = node
                    } else {
                        contexts[f.name] = TextNode.valueOf(id)
                    }
                }
            }
            currentClass = currentClass.superclass
            if (!namespace.isPresent) {
                namespace = Optional.ofNullable(currentClass.getAnnotation(JsonldNamespace::class.java))
            }
        }
        return contexts
    }

    private fun generateNamespaces(objType: Class<*>): Map<String, String> {
        val namespaceAnnotations = objType.getAnnotationsByType(JsonldNamespace::class.java)
        val namespaces: MutableMap<String, String> = HashMap(namespaceAnnotations.size)
        mutableListOf(*namespaceAnnotations).forEach(Consumer { ns: JsonldNamespace -> namespaces[ns.name] = ns.uri })
        return namespaces
    }

    private fun buildMultiContext(context: String, generatedContext: JsonNode): ArrayNode {
        return JsonNodeFactory.withExactBigDecimals(true).arrayNode().add(context).add(generatedContext)
    }
}