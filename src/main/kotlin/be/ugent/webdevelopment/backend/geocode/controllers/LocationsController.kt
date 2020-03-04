package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.model.Location
import be.ugent.webdevelopment.backend.geocode.services.LocationsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/locations")
class LocationsController : Controller<Location, UUID> {

    @Autowired
    private lateinit var service: LocationsService

    override fun findAll(): List<Location> {
        return service.findAll()
    }

    override fun findById(id: UUID): Location {
        return service.findById(id)
    }

    override fun create(resource: Location): UUID{
        return service.create(resource)
    }

    override fun update(id: UUID, resource: Location) {
        service.update(id, resource)
    }

    override fun delete(id: UUID) {
        service.deleteById(id)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}