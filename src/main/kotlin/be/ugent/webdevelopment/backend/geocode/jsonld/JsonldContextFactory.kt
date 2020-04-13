package be.ugent.webdevelopment.backend.geocode.jsonld

import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.*
import be.ugent.webdevelopment.backend.geocode.jsonld.util.AnnotationsUtils
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonUtils
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import io.swagger.v3.core.util.Json
import org.apache.commons.lang3.ClassUtils
import java.lang.reflect.AccessibleObject
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Stream
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * @author Alexander De Leon
 */
object JsonldContextFactory {

    fun fromPackage(packageName: String?, provider: SerializerProvider?): ObjectNode {
        val generatedContext = JsonNodeFactory.withExactBigDecimals(true).objectNode()
        val scanner = FastClasspathScanner(packageName)
        scanner.matchAllStandardClasses { clazz: Class<*> ->
            if (!Modifier.isAbstract(clazz.modifiers) && AnnotationsUtils.isAnnotationPresent(clazz, JsonldTypeFromJavaClass::class.java)) {
                val type = JsonldResourceUtils.dynamicTypeLookup(clazz)
                type.ifPresent { t: String? -> generatedContext.set<JsonNode>(clazz.simpleName, TextNode.valueOf(t)) }
            }
            if (AnnotationsUtils.isAnnotationPresent(clazz, JsonldResource::class.java)) {
                val resourceContext = fromAnnotations(clazz, provider)
                resourceContext.ifPresent { context: JsonNode -> JsonUtils.merge(generatedContext, context) }
            }
        }
        scanner.scan()
        return JsonNodeFactory.withExactBigDecimals(true).objectNode().set<JsonNode>("@context", generatedContext) as ObjectNode
    }

    fun fromAnnotations(instance: JvmType.Object, provider: SerializerProvider?): Optional<ObjectNode> {
        return fromAnnotations(instance.javaClass, provider)
    }

    fun fromAnnotations(instances: Iterable<*>, provider: SerializerProvider?): Optional<ObjectNode> {
        val mergedContext = JsonNodeFactory.withExactBigDecimals(true).objectNode()
        instances.forEach { e: Any? -> e?.let { fromAnnotations(it.javaClass, provider).map(Function<ObjectNode, Any> { other: ObjectNode? -> mergedContext.setAll(other) }) } }
        return if (mergedContext.size() != 0) Optional.of(mergedContext) else Optional.empty()
    }

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
        System.out.println("IN the generator, JsonView= " + provider?.activeView?.name)
        //System.out.println("IN the generator, Class= " + currentClass.name)
        var namespace: Optional<JsonldNamespace> = Optional.ofNullable(currentClass.getAnnotation(JsonldNamespace::class.java))
        while (currentClass != Any::class.java) {
            val fields = currentClass.declaredFields
            for (f in fields) {
                if (f.isAnnotationPresent(JsonldId::class.java) || f.name == "this$0") {
                    //System.out.println("Skipped in JsonldId if")
                    contexts["@id"] = TextNode.valueOf(f.name)
                    continue
                }
                val jsonldProperty = f.getAnnotation(JsonldProperty::class.java)
                var propertyId: Optional<String> = Optional.empty()
                // Most concrete field overrides any field with the same name defined higher up the hierarchy
                if (jsonldProperty != null) {
                    propertyId = Optional.of(jsonldProperty.value)
                } else {
                    //System.out.println("Field: " + f.name + ". Of class: " + currentClass.name + ". Does not have a JsonldProperty.")
                }
                val className = currentClass.name
                propertyId.ifPresent { id: String? ->
                    if (f.isAnnotationPresent(JsonldId::class.java) ||
                            ClassUtils.getAllSuperclasses(f.type).any {
                                t -> t.declaredFields.any {
                                df -> df.isAnnotationPresent(JsonldId::class.java)} }) {
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

    private fun relationType(field: Field): Class<*> {
        var type = field.type
        if (MutableCollection::class.java.isAssignableFrom(type)) {
            val parameterizedType = field.genericType as ParameterizedType
            val t = parameterizedType.actualTypeArguments[0]
            if (Class::class.java.isAssignableFrom(t.javaClass)) {
                type = t as Class<*>
            } else if (ParameterizedType::class.java.isAssignableFrom(t.javaClass)) {
                type = (t as ParameterizedType).rawType as Class<*>
            }
        }
        if (type.isArray) {
            type = type.componentType
        }
        return type
    }

    private fun generateNamespaces(objType: Class<*>): Map<String, String> {
        val namespaceAnnotations = objType.getAnnotationsByType(JsonldNamespace::class.java)
        val namespaces: MutableMap<String, String> = HashMap(namespaceAnnotations.size)
        mutableListOf(*namespaceAnnotations).forEach(Consumer { ns: JsonldNamespace -> namespaces[ns.name] = ns.uri })
        return namespaces
    }

    fun multiContext(externalContext: Optional<String?>,
                     internalContext: Optional<ObjectNode?>): Optional<JsonNode> {
        return if (internalContext.isPresent) {
            externalContext.map { s: String? -> Optional.of((s?.let { buildMultiContext(it, internalContext.get()) } as JsonNode)) }.orElseGet { internalContext.map { it: ObjectNode? -> it } }
        } else externalContext.map { v: String? -> TextNode.valueOf(v) }
    }

    private fun buildMultiContext(context: String, generatedContext: JsonNode): ArrayNode {
        return JsonNodeFactory.withExactBigDecimals(true).arrayNode().add(context).add(generatedContext)
    }
}