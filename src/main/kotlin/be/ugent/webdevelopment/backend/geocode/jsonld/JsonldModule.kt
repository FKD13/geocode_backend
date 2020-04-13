package be.ugent.webdevelopment.backend.geocode.jsonld

import be.ugent.webdevelopment.backend.geocode.jsonld.internal.JsonldBeanDeserializerModifier
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.JsonldPropertyNamingStrategy
import be.ugent.webdevelopment.backend.geocode.jsonld.internal.JsonldResourceSerializerModifier
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import java.util.function.Supplier

/**
 * @author Alexander De Leon
 */
class JsonldModule @JvmOverloads constructor(contextSupplier: Supplier<Any> = Supplier { return@Supplier }) : SimpleModule() { // Don't acually know if return@Supplier works
    /**
     * Creates a JsonldModule configured with a Jsonld Context specified in the context argument (Json Object)
     * @param context an ObjectNode with the structure of your default @context
     */
    constructor(context: JsonNode) : this(Supplier<Any> { context }) {}

    /**
     * Creates a JsonldModule configured with a Jsonld Context specified in the context argument (Map)
     * @param context a Map with the structure of your default @context
     */
    constructor(context: Map<String?, Any?>) : this(Supplier<Any> { context }) {}
    /**
     * Create a JsonldModule configured with a function which supplies the @context structure of your application.
     * This constructor is useful if you want to construct your context dynamically. If the context is static is better to use the other constructors of this class.
     *
     * @param contextSupplier a function from () to Object which supplies the default Jsonld context of your application.
     */
    /**
     * Creates a JsonldModule configured with an empty application context.
     */
    init {
        setNamingStrategy(JsonldPropertyNamingStrategy())
        setDeserializerModifier(JsonldBeanDeserializerModifier(contextSupplier))
        setSerializerModifier(JsonldResourceSerializerModifier())
    }
}