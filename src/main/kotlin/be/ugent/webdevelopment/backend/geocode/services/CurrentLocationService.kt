package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.CurrentLocationReceiveWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.CurrentLocationWrapper
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class CurrentLocationService {

    /**
     * Get the current location based on the received input IP.
     * @param ip IP address to get the location for.
     */
    fun getCurrentLocation(ip: String): CurrentLocationWrapper {
        // This is HTTP since HTTPS requires a payment plan.
        val ipApiUrl = URI.create("http://ip-api.com/json/");

        // Do the request
        try {
            val currentLocationReceive: CurrentLocationReceiveWrapper =
                    RestTemplate().getForObject(ipApiUrl, CurrentLocationReceiveWrapper::class.java)
                    ?: throw GenericException("Unable to fetch current location.")

            return CurrentLocationWrapper(
                    currentLocationReceive.lat,
                    currentLocationReceive.lon
            )
        } catch (exception: Exception) {
            throw GenericException("Unable to fetch current location.")
        }
    }
}