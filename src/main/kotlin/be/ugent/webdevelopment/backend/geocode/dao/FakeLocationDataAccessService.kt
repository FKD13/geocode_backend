package be.ugent.webdevelopment.backend.geocode.dao

import be.ugent.webdevelopment.backend.geocode.model.Location
import org.springframework.stereotype.Repository
import java.util.*

@Repository("fakeLocationDao")
class FakeLocationDataAccessService : LocationDao{

    private var db: ArrayList<Location> = ArrayList()

    override fun insertLocation(id: UUID, location: Location): UUID {
        db.add(Location(id, location))
        return id
    }

    override fun getAllLocations(): List<Location> {
        return db
    }

    override fun getLocationById(id: UUID): Location? {
        var res: Location? = null
        for (location: Location in db){
            if (location.secret_id == id){
                res = location
                break
            }
        }
        return res
    }

    override fun updateLocation(id: UUID, location: Location): Int {
        val internalLocation : Location? = getLocationById(id)
        return if(internalLocation == null){
            -1
        }else{
            if (internalLocation.creator_id != null){
                internalLocation.creator_id = location.creator_id
            }
            if (internalLocation.long != null){
                internalLocation.long = location.long
            }
            if (internalLocation.lat != null){
                internalLocation.lat = location.lat
            }
            if (internalLocation.secret_id != null){
                internalLocation.secret_id = location.secret_id
            }
            if (internalLocation.time != null){
                internalLocation.time = location.time
            }
            if (internalLocation.listed != null){
                internalLocation.listed = location.listed
            }
            if (internalLocation.name != null){
                internalLocation.name = location.name
            }
            if (internalLocation.description != null){
                internalLocation.description = location.description
            }
            1
        }    }

    override fun deleteLocation(id: UUID): Int {
        val location: Location? = getLocationById(id)
        return if (location == null){
            -1
        }else{
            if (db.remove(location)){
                1
            }else{
                -1
            }
        }
    }


}