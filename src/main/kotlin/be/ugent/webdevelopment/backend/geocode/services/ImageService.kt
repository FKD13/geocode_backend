package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.database.repositories.ReportRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest


@Service
class ImageService {

    @Autowired
    lateinit var reportService: ReportService

    @Autowired
    lateinit var imageRepository: ImageRepository

    @Autowired
    lateinit var reportRepository: ReportRepository

    @Transactional
    fun saveImageFile(file: MultipartFile): Int {
        val byteObjects = Array<Byte>(file.size.toInt()) { 0 }
        var i = 0
        for (b in file.bytes) {
            byteObjects[i++] = b
        }
        return imageRepository.saveAndFlush(Image(image = byteObjects, contentType = file.contentType!!)).id
    }

    @Transactional
    fun getImages(id: Int, request: HttpServletRequest): Image {
        val image = imageRepository.findById(id)
        when {
            image.isPresent -> {
                return if (reportRepository.findAllByImage(image = image.get()).isPresent) {
                    reportService.checkAdmin(request)
                    image.get()
                } else {
                    image.get()
                }
            }
            else -> {
                throw GenericException("The Image associated with the id = $id was not found.")
            }
        }
    }

    fun checkImageId(property: String, imageId: Int?, container: ExceptionContainer) {
        if (imageId != null) {
            if (imageRepository.findById(imageId).isEmpty) {
                container.addException(PropertyException(property, "The given $property was not found in the database."))
            }
        }
    }

}