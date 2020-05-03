package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.LeaderboardsWrapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/leaderboards")
@ResponseStatus(HttpStatus.OK)
class LeaderboardsController {

    @GetMapping("/visitedLocations")
    fun visitedLocations(@PathParam("limit") limit: Int): List<LeaderboardsWrapper> {
        return emptyList()
    }

    @GetMapping("/visitedTours")
    fun visitedTours(@PathParam("limit") limit: Int): List<LeaderboardsWrapper> {
        return emptyList()
    }

    @GetMapping("/visitedCountries")
    fun visitedCountries(@PathParam("limit") limit: Int): List<LeaderboardsWrapper> {
        return emptyList()
    }

    @GetMapping("/createdLocations")
    fun createdLocations(@PathParam("limit") limit: Int): List<LeaderboardsWrapper> {
        return emptyList()
    }

    @GetMapping("/createdTours")
    fun createdTours(@PathParam("limit") limit: Int): List<LeaderboardsWrapper> {
        return emptyList()
    }
}