package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.Location
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.services.JWTAuthenticator
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/locations")
class LocationsController(val service : LocationsService, val jwtService: JWTAuthenticator) : Controller<LocationsWrapper>{

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @JsonView(View.PublicDetail::class)
    fun findAll(response: HttpServletResponse, request: HttpServletRequest): List<Location> {
        return service.findAll()
    }

    @GetMapping(value = ["/{secret_id}"])
    fun findById(@PathVariable secret_id: UUID,
                 response: HttpServletResponse, request: HttpServletRequest): LocationsWrapper {
        return service.findBySecretId(secret_id)
    }

    @PostMapping
    fun create(@RequestBody resource: LocationsWrapper,
               response: HttpServletResponse, request: HttpServletRequest): UUID{
        resource.creatorId = Optional.of(jwtService.tryAuthenticate(request).id)
        return service.create(resource)
    }

    @PatchMapping(value = ["/{secret_id}"])
    fun update(@PathVariable secret_id: UUID, @RequestBody resource: LocationWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        if(jwtService.tryAuthenticate(request).id != service.findBySecretId(secret_id).creatorId.get())
            throw GenericException("The currently logged in user did not create this location and can therefor not edit it.")
        service.update(secret_id, resource)
    }

    @DeleteMapping(value = ["/{secret_id}"])
    fun delete(@PathVariable secret_id: UUID,
               response: HttpServletResponse, request: HttpServletRequest) {
        service.deleteById(jwtService.tryAuthenticate(request), secret_id)
    }
}