package be.ugent.webdevelopment.backend.geocode

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GeocodeApplication

fun main(args: Array<String>) {
    runApplication<GeocodeApplication>(*args)
}
