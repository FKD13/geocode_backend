package be.ugent.webdevelopment.backend.geocode.controllers

import be.ugent.webdevelopment.backend.geocode.services.ImageService
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/image")
class ImageController(
        var imageService: ImageService
) {

    @GetMapping("/{id}")
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
}