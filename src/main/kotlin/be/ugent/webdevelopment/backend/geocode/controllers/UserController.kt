package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.*
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.database.models.Tour
import be.ugent.webdevelopment.backend.geocode.database.models.User
import be.ugent.webdevelopment.backend.geocode.services.*
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("/user")
@JsonView(View.PrivateDetail::class)
class UserController(
        val usersService: UsersService,
        val jwtService: JWTAuthenticator,
        val locationsService: LocationsService,
        val visitsService: VisitsService,
        val statisticsService: StatisticsService,
        val imageService: ImageService,
        val authService: AuthService,
        val toursService: ToursService,
        val achievementService: AchievementService
) {

    @GetMapping
    fun findByLoggedIn(
            response: HttpServletResponse, request: HttpServletRequest): User {
        return jwtService.tryAuthenticate(request)
    }

    @GetMapping(value = ["/locations"])
    @JsonView(View.List::class)
    fun getLocations(response: HttpServletResponse, request: HttpServletRequest): List<ExtendedLocationWrapper> {
        return locationsService.findAllByUser(jwtService.tryAuthenticate(request))
    }

    @PatchMapping
    fun update(@RequestBody resource: UserWrapper,
               response: HttpServletResponse, request: HttpServletRequest) {
        usersService.update(jwtService.tryAuthenticate(request), resource)
    }

    @DeleteMapping
    fun delete(response: HttpServletResponse, request: HttpServletRequest) {
        usersService.deleteUser(jwtService.tryAuthenticate(request))
    }

    @DeleteMapping("/data")
    fun deleteData(@RequestBody resource: DeleteWrappper, response: HttpServletResponse, request: HttpServletRequest) {
        usersService.deleteData(jwtService.tryAuthenticate(request), resource)
    }

    @PostMapping("/avatar")
    fun avatarUpload(@RequestBody image: MultipartFile, request: HttpServletRequest, response: HttpServletResponse): Int {
        return imageService.saveImageFile(image)
    }

    @PatchMapping("/passwordreset")
    fun passWordReset(@RequestBody resource: ResetWrapper, response: HttpServletResponse, request: HttpServletRequest) {
        authService.passwordReset(resource, jwtService.tryAuthenticate(request))
    }

    @PatchMapping("/privacy")
    fun privacySettings(@RequestBody resource: PrivacyWrapper, response: HttpServletResponse, request: HttpServletRequest) {
        usersService.privacy(resource, jwtService.tryAuthenticate(request))
    }

    //------------------------------------------------------------------------------------------------------------------
    // Visits

    @GetMapping("/visits")
    fun getVisitsForUser(response: HttpServletResponse, request: HttpServletRequest): List<CheckIn> {
        return visitsService.getVisitsByUser(jwtService.tryAuthenticate(request))
    }

    @GetMapping("/visits/{secretId}")
    fun getVisitsForUserByLocationSecret(@PathVariable secretId: UUID,
                                         response: HttpServletResponse, request: HttpServletRequest): List<CheckIn> {
        return visitsService.getVisitsByUserForLocation(jwtService.tryAuthenticate(request), secretId)
    }

    //------------------------------------------------------------------------------------------------------------------
    // Statistics

    @GetMapping("/statistics")
    fun getUserStatistics(request: HttpServletRequest, response: HttpServletResponse): UserStatistics {
        return statisticsService.getUserStatistics(jwtService.tryAuthenticate(request))
    }

    //------------------------------------------------------------------------------------------------------------------
    // Tours

    @GetMapping("/tours")
    fun getUserTours(request: HttpServletRequest, response: HttpServletResponse): List<Tour> {
        return toursService.getByUser(jwtService.tryAuthenticate(request))
    }

    //------------------------------------------------------------------------------------------------------------------
    // Achievements

    @GetMapping("/achievements")
    fun getUserAchievements(request: HttpServletRequest): List<UserAchievementWrapper> {
        return achievementService.getUserAchievementsWrapper(jwtService.tryAuthenticate(request))
    }

}