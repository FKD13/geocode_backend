package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage

@Service
class QRCodeService {

    /**
     * Return a QRCode given the locations visit secret
     * @param visitSecret the visit secret of the location
     * @param frontendUrl the url of the frontend to add to the link
     * @param size the size in pixels of the image
     * @return a BufferedImage representing the qr code
     */
    fun getQRCode(visitSecret: String, frontendUrl: String, size: Int): BufferedImage {
        // A little safe check
        val url = if (frontendUrl.endsWith("/")) frontendUrl + visitSecret else "$frontendUrl/$visitSecret"
        if (size > 1024) {
            throw GenericException("Size is to big, should be maximum 1024.")
        }
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)

        return MatrixToImageWriter.toBufferedImage(bitMatrix)
    }
}