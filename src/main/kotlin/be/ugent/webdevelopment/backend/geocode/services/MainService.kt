package be.ugent.webdevelopment.backend.geocode.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.nio.charset.Charset


@Service
class MainService {

    @Value("classpath:privacy_agreement.html")
    private lateinit var privacyAgreement: Resource

    fun getPrivacyAgreement(): String {
        try {
            InputStreamReader(privacyAgreement.inputStream, Charset.defaultCharset()).use {
                return FileCopyUtils.copyToString(it)
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }
}