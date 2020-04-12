package be.ugent.webdevelopment.backend.geocode

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import cz.cvut.kbss.jsonld.ConfigParam
import cz.cvut.kbss.jsonld.JsonLd
import cz.cvut.kbss.jsonld.jackson.JsonLdModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class RestConfig : WebMvcConfigurationSupport() {

    @Bean(name = ["jsonLdMapper"])
    fun jsonLdObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // Here we register the JSON-LD serialization/deserialization module
        val module = JsonLdModule()
        // Package scan is important for polymorphic deserialization
        module.configure(ConfigParam.SCAN_PACKAGE, "be.ugent.webdevelopment.backend.geocode")
        objectMapper.registerModule(module)
        return objectMapper
    }

    @Primary
    @Bean(name = ["objectMapper"])
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return objectMapper
    }

    public override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(createJsonLdMessageConverter())
        super.addDefaultHttpMessageConverters(converters)
    }

    private fun createJsonLdMessageConverter(): HttpMessageConverter<*> {
        val converter = MappingJackson2HttpMessageConverter(
                jsonLdObjectMapper())
        converter.supportedMediaTypes = listOf(MediaType.valueOf(JsonLd.MEDIA_TYPE))
        return converter
    }
}
