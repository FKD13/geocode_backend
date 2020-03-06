package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.dao.LocationDao
import be.ugent.webdevelopment.backend.geocode.model.Location
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationsService: ServiceInterface<Location, UUID>{

    @Autowired
    @Qualifier("fakeLocationDao")
    private lateinit var locationDao: LocationDao

    override fun findAll(): List<Location> {
        return locationDao.getAllLocations()
    }

    override fun findById(id: UUID): Location {
        return locationDao.getLocationById(id)!! //TODO dit moet mooier worden en een http error gooien
    }

    override fun create(resource: Location): UUID {
        return locationDao.insertLocation(resource)
    }

    override fun update(id: UUID, resource: Location): Int {
        return locationDao.updateLocation(id, resource)
    }

    override fun deleteById(id: UUID): Int{
        return locationDao.deleteLocation(id)
    }

}