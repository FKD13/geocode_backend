package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.services.LocationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RequestMapping("/Location")
@ResponseStatus(HttpStatus.OK)
@RestController
class LocationController(val service: LocationService) : Controller<LocationWrapper>{

    @GetMapping
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<LocationWrapper> {
        return service.findAll(1)
    }

    @PostMapping
    fun create(@RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest): UUID{
        return service.create(resource)
    }

    @PutMapping(value = ["/{secret_id}"])
    fun update(@PathVariable secret_id: UUID, @RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.update(secret_id, resource)
    }

    @DeleteMapping(value = ["/{secret_id}"])
    fun delete(@PathVariable secret_id: UUID,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteById(secret_id)
    }

}