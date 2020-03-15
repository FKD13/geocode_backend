package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RequestMapping("/Location")
@ResponseStatus(HttpStatus.OK)
@RestController
class LocationController(val service: LocationService, val jwtService: JWTAuthenticator) : Controller<LocationWrapper>{

    @GetMapping
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<LocationWrapper> {
        return service.findAll(jwtService.tryAuthenticateGetId(request))
    }

    @PostMapping
    fun create(@RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest): UUID{
        resource.creatorId = Optional.of(jwtService.tryAuthenticateGetId(request))
        return service.create(resource)
    }

    @PutMapping(value = ["/{secret_id}"])
    fun update(@PathVariable secret_id: UUID, @RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        //todo double check of dat de location met die secret_id ook effectief van de ingelogde user is.
        service.update(secret_id, resource)
    }

    @DeleteMapping(value = ["/{secret_id}"])
    fun delete(@PathVariable secret_id: UUID,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteById(secret_id)
    }

}