package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.GeneralStatistics
import be.ugent.webdevelopment.backend.geocode.services.StatisticsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@ResponseStatus(HttpStatus.OK)
class StatisticsController(val statisticsService: StatisticsService) {

    @GetMapping
    fun getStatistics() : GeneralStatistics{
        return statisticsService.getStatistics()
    }
}