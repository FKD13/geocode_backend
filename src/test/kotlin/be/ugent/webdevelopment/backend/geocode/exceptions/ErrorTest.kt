package be.ugent.webdevelopment.backend.geocode.exceptions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ErrorTest {

    @Test
    fun `PropertyException should contain the same`() {
        val ex = PropertyException("name", "not a valid name")
        val exw = ex.wrap()
        Assertions.assertEquals(ex.field, exw.field)
        Assertions.assertEquals(ex.message, exw.message)
    }

    @Test
    fun `GenericException should contain the same`() {
        val ex = GenericException("nonono")
        val exw = ex.wrap()
        Assertions.assertEquals(ex.message, exw.message)
    }

    @Test
    fun `Container size should be the same after wrap`() {
        val ex = ExceptionContainer()
        for (i: Int in 1..3) {
            ex.addException(GenericException("$i"))
            ex.addException(PropertyException("$i", "$i, $i"))
        }
        val exw = ex.wrap()
        Assertions.assertEquals(ex.inputExceptions.size, exw.inputErrors.size)
        Assertions.assertEquals(ex.generalExceptions.size, exw.generalErrors.size)
    }
}