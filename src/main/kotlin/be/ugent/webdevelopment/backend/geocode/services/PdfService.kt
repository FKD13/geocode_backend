package be.ugent.webdevelopment.backend.geocode.services

import be.ugent.webdevelopment.backend.geocode.exceptions.GenericException
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class PdfService(
        val qrCodeService: QRCodeService
) {
    fun getPdf(bufferedImage: BufferedImage) : ByteArrayInputStream {
        val document = Document()

        val baoDoc = ByteArrayOutputStream()
        val bao = ByteArrayOutputStream()

        // write image to steam
        ImageIO.write(bufferedImage, "png", bao)

        val image = Image.getInstance(bao.toByteArray())

        try {

            val table = PdfPTable(1)

            table.widthPercentage = 100F

            val cell = PdfPCell(Phrase("GEOCODE", FontFactory.getFont(FontFactory.HELVETICA_BOLD)))
            cell.horizontalAlignment = Element.ALIGN_CENTER

            table.addCell(image)
            table.addCell(cell)

            PdfWriter.getInstance(document, baoDoc)

            document.open()
            document.add(table)
            document.close()

        } catch (ex: DocumentException) {
            LoggerFactory.getLogger(this::class.java).error("Error while generating pdf", ex)
            throw GenericException("An Error occurred while generating pdf")
        }

        return ByteArrayInputStream(baoDoc.toByteArray())
    }
}