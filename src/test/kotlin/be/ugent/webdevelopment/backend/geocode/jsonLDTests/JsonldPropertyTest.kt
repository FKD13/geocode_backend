package be.ugent.webdevelopment.backend.geocode.jsonLDTests

import be.ugent.webdevelopment.backend.geocode.jsonld.JsonldContextFactory
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldProperty
import be.ugent.webdevelopment.backend.geocode.jsonld.annotation.JsonldType
import org.junit.jupiter.api.Test

class JsonldPropertyTest {

    @Test
    fun contextTest(){
        val result = JsonldContextFactory.fromAnnotations(TestClass("whoopsiedaisie").javaClass, null)
        println(result)
    }

}


class TestClass constructor( @field:JsonldProperty(value = "WOOHOOOO") var test :String){

}