package be.ugent.webdevelopment.backend.geocode.services

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage

@Service
class QRCodeService {
    /*
    * Then frontend url that should be uses in the url path
    * */
    @Value("\${GEOCODE_FRONTEND_URL}")
    private lateinit var frontendUrl: String

    private final val visitUrl = "locations/visits/"

    /*
    * The size in pixels of the qrcode
    * */
    @Value("\${GEOCODE_QRCODE_SIZE}")
    private lateinit var qrcodeSize: String

    /*
    * Return a QRCode given the locations visit secret
    * */
    fun getQRCode(visitSecret: String) : BufferedImage {
        // A little safe check
        val url = if (frontendUrl.endsWith("/")) {
            "${frontendUrl}${visitUrl}${visitSecret}"
        } else {
            "${frontendUrl}/${visitUrl}${visitSecret}"
        }

        val size = qrcodeSize.toIntOrNull() ?: throw IllegalArgumentException("GEOCODE_QRCODE_SIZE was not an Int")

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size)

        return MatrixToImageWriter.toBufferedImage(bitMatrix)
    }
}