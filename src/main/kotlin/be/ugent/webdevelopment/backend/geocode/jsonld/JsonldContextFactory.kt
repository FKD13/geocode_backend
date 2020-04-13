package be.ugent.webdevelopment.backend.geocode.jsonld

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldId
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldLink
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldNamespace
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import org.apache.commons.lang3.ClassUtils
import java.util.*
import java.util.function.Consumer

/**
 * @author Alexander De Leon
 */
object JsonldContextFactory {

    fun fromAnnotations(objType: Class<*>, provider: SerializerProvider?): Optional<ObjectNode> {
        val generatedContext = JsonNodeFactory.withExactBigDecimals(true).objectNode()
        generateNamespaces(objType).forEach { (name: String?, uri: String?) -> generatedContext.set<JsonNode>(name, TextNode(uri)) }
        //TODO: This is bad...it does not consider other Jackson annotations. Need to use a AnnotationIntrospector?
        val fieldContexts = generateContextsForFields(objType, provider)
        fieldContexts.forEach { (fieldName: String?, value: JsonNode?) -> generatedContext.set<JsonNode>(fieldName, value) }
        //add links
        val links = objType.getAnnotationsByType(JsonldLink::class.java)
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

    private fun generateContextsForFields(objType: Class<*>, provider: SerializerProvider?): Map<String, JsonNode> {
        return generateContextsForFields(objType, ArrayList(), provider)
    }

    private fun generateContextsForFields(objType: Class<*>, ignoreTypes: List<Class<*>>, provider: SerializerProvider?): Map<String, JsonNode> {
        val contexts: MutableMap<String, JsonNode> = HashMap()
        var currentClass: Class<*> = objType
        var namespace: Optional<JsonldNamespace> = Optional.ofNullable(currentClass.getAnnotation(JsonldNamespace::class.java))
        while (currentClass != Any::class.java) {
            val fields = currentClass.declaredFields
            for (f in fields) {
                if (f.isAnnotationPresent(JsonldId::class.java) || f.name == "this$0") {
                    contexts["@id"] = TextNode.valueOf(f.name)
                    continue
                }
                val jsonldProperty = f.getAnnotation(JsonldProperty::class.java)

                var propertyId: Optional<String> = Optional.empty()
                if (jsonldProperty != null) {
                    propertyId = Optional.of(jsonldProperty.value)
                }
                val className = currentClass.name
                propertyId.ifPresent { id: String? ->
                    if (f.isAnnotationPresent(JsonldId::class.java) ||
                            ClassUtils.getAllSuperclasses(f.type).any { t ->
                                t.declaredFields.any { df -> df.isAnnotationPresent(JsonldId::class.java) }
                            }) {
                        //System.out.println("isRelation returned true for Field: " + f.name + ". Of class: " + className)
                        val node = JsonNodeFactory.withExactBigDecimals(true).objectNode()
                        node.set<JsonNode>("@id", TextNode.valueOf(id))
                        node.set<JsonNode>("@type", TextNode.valueOf("@id"))
                        contexts[f.name] = node
                        System.out.println("NODE=" + node)
                    } else {
                        //System.out.println("isRelation returned false for Field: " + f.name + ". Of class: " + className)
                        contexts[f.name] = TextNode.valueOf(id)
                        System.out.println("context: " + f.name + " and " + TextNode.valueOf(id))
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