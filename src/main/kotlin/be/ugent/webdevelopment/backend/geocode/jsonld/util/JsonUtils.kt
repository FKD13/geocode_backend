package be.ugent.webdevelopment.backend.geocode.jsonld.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * @author Alexander De Leon (alex.deleon@devialab.com)
 */
object JsonUtils {
    fun merge(mainNode: JsonNode, updateNode: JsonNode): JsonNode {
        val fieldNames = updateNode.fieldNames()
        while (fieldNames.hasNext()) {
            val fieldName = fieldNames.next()
            val jsonNode = mainNode[fieldName]
            // if field exists and is an embedded object
            if (jsonNode != null && jsonNode.isObject) {
                JsonUtils.merge(jsonNode, updateNode[fieldName])
            } else {
                if (mainNode is ObjectNode) {
                    // Overwrite field
                    val value = updateNode[fieldName]
                    mainNode.set<JsonNode>(fieldName, value)
                }
            }
        }
        return mainNode
    }
}