package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedLocationWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.ExtendedUserWrapper
import be.ugent.webdevelopment.backend.geocode.controllers.wrappers.UserWrapper
import be.ugent.webdevelopment.backend.geocode.database.View
import be.ugent.webdevelopment.backend.geocode.database.models.CheckIn
import be.ugent.webdevelopment.backend.geocode.services.*
import com.fasterxml.jackson.annotation.JsonView
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
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
        val imageService: ImageService) {

    @GetMapping
    fun findByLoggedIn(
            response: HttpServletResponse, request: HttpServletRequest): ExtendedUserWrapper {
        val user = jwtService.tryAuthenticate(request)
        return ExtendedUserWrapper(user, imageService.getUrlForImage("user/avatar", user.avatarId))
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

    @PostMapping("/avatar")
    fun avatarUpload(@RequestBody image: MultipartFile, request: HttpServletRequest, response: HttpServletResponse): Int {
        return imageService.saveImageFile(image)
    }

    @GetMapping("/avatar/{id}")
    fun getImagesForTesting(@PathVariable id: Int, request: HttpServletRequest, response: HttpServletResponse) {
        val image = imageService.getImages(id)
        val byteArray = ByteArray(image.image.size)
        var i = 0

        for (wrappedByte in image.image) {
            byteArray[i++] = wrappedByte //auto unboxing
        }

        response.contentType = image.contentType
        val inputStream: InputStream = ByteArrayInputStream(byteArray)
        IOUtils.copy(inputStream, response.outputStream)
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
}