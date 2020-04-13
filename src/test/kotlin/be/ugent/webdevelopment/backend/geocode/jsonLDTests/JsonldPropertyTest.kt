package be.ugent.webdevelopment.backend.geocode.jsonLDTests

import ioinformarics.oss.jackson.module.jsonld.JsonldContextFactory
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldType
import org.junit.jupiter.api.Test

class JsonldPropertyTest {

    @Test
    fun contextTest(){
        val result = JsonldContextFactory.fromAnnotations(TestClass("PLEASE WERK"))
        println(result)
    }

}


@JsonldType("http://schema.org/Place")
class TestClass{

    @JsonldProperty(value = "WOOHOOOO")
    var test :String = "WOOOHOOOOO"

    constructor(test : String = "WOOOHOOOOO"){
        this.test = test
    }

}