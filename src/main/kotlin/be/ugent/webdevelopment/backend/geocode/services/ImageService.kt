package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.database.models.Image
import be.ugent.webdevelopment.backend.geocode.database.repositories.ImageRepository
import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
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
        val byteObjects = Array<Byte>(file.size.toInt()) {0}
        var i = 0
        for (b in file.bytes) {
            byteObjects[i++] = b
        }
        return imageRepository.save(Image(image = byteObjects)).id
    }

    @Transactional
    fun getImages(id : Int): Array<Byte> {
        val image = imageRepository.findById(id)
        if(image.isPresent){
            return image.get().image
        }else{
            throw GenericException("The Image associated with the id = $id was not found.")
        }
    }

}