package be.ugent.webdevelopment.backend.geocode.jsonLDTests

import be.ugent.webdevelopment.backend.geocode.database.models.*
import ioinformarics.oss.jackson.module.jsonld.JsonldContextFactory
import org.junit.jupiter.api.Test

class JsonldPropertyTest {

    @Test
    fun contextTest(){
        val result = JsonldContextFactory.fromAnnotations(TestClass())
        println(result)
    }

}