package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.ExceptionContainer
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import be.ugent.webdevelopment.backend.geocode.exceptions.PropertyException
import be.ugent.webdevelopment.backend.geocode.jsonld.util.JsonldResourceUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


@Service
class ImageService {

    @Autowired
    lateinit var imageRepository: ImageRepository

    @Transactional
    fun saveImageFile(file: MultipartFile): Int {
        val byteObjects = Array<Byte>(file.size.toInt()) { 0 }
        var i = 0
        for (b in file.bytes) {
            byteObjects[i++] = b
        }
        return imageRepository.save(Image(image = byteObjects, contentType = file.contentType!!)).id
    }

    @Transactional
    fun getImages(id: Int): Image {
        val image = imageRepository.findById(id)
        if (image.isPresent) {
            return image.get()
        } else {
            throw GenericException("The Image associated with the id = $id was not found.")
        }
    }

    fun checkImageId(property: String, imageId: Int?, container: ExceptionContainer) {
        if (imageId != null) {
            if (imageRepository.findById(imageId).isEmpty) {
                container.addException(PropertyException("avatarId", "The given avatarId was not found in the database."))
            }
        }
    }

    fun getUrlForImage(prefix: String, imageId: Int?): String {
        if (imageId == null) {
            return ""
        }
        return JsonldResourceUtils.appendIfNeeded(System.getenv("GEOCODE_BACKEND_URL"), "/") + JsonldResourceUtils.appendIfNeeded(prefix, "/") + imageId.toString()
    }

}