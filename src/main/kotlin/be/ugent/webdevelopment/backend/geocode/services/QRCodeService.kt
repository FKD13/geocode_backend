package be.ugent.webdevelopment.backend.geocode.services

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
     * @return a Bufferedimage representing the qrcode
     */
    fun getQRCode(visitSecret: String, frontendUrl: String, size: Int): BufferedImage {
        // A little safe check
        val url = if (frontendUrl.endsWith("/")) frontendUrl + visitSecret else "$frontendUrl/$visitSecret"

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)

        return MatrixToImageWriter.toBufferedImage(bitMatrix)
    }
}