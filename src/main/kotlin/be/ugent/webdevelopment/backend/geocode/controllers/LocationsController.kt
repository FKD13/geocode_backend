package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LocationsWrapper
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/locations")
class LocationsController : Controller<LocationsWrapper, UUID> {

    @Autowired
    private lateinit var service: LocationsService

    override fun findAll(): List<LocationsWrapper> {
        return service.findAll()
    }

    override fun findById(id: UUID): LocationsWrapper {
        return service.findById(id)
    }

    override fun create(resource: LocationsWrapper): UUID{
        return service.create(resource)
    }

    override fun update(id: UUID, resource: LocationsWrapper) {
        service.update(id, resource)
    }

    override fun delete(id: UUID) {
        service.deleteById(id)
    }

}