package be.ugent.webdevelopment.backend.geocode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = arrayOf(ErrorMvcAutoConfiguration::class))
class GeocodeApplication

fun main(args: Array<String>) {
    runApplication<GeocodeApplication>(*args)
}
