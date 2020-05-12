package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.database.models.Achievement
import be.ugent.webdevelopment.backend.geocode.services.AchievementService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/achievements")
class AchievementController(
        val achievementService: AchievementService
) {
    @GetMapping
    fun getAchievements(): List<Achievement> {
        return achievementService.getAchievements()
    }
}