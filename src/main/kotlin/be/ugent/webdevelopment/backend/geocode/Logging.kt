package be.ugent.webdevelopment.backend.geocode

import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.math.min

@Configuration
class DoogiesRequestLogger : OncePerRequestFilter() {

    private val includeResponsePayload = true
    private val maxPayloadLength = 1000000

    private fun getContentAsString(buf: ByteArray?, charsetName: String): String {
        if (buf == null || buf.isEmpty()) return ""
        val length = min(buf.size, maxPayloadLength)
        return try {
            String(buf, 0, length, Charset.forName(charsetName))
        } catch (ex: UnsupportedEncodingException) {
            "Unsupported Encoding"
        }
    }

    /**
     * Log each request and respponse with full Request URI, content payload and duration of the request in ms.
     * @param request the request
     * @param response the response
     * @param filterChain chain of filters
     * @throws ServletException
     * @throws IOException
     */
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val startTime = System.currentTimeMillis()
        val reqInfo = StringBuffer()
                .append("[")
                .append(startTime % 10000) // request ID
                .append("] ")
                .append(request.method)
                .append(" ")
                .append(request.requestURL)
        val queryString = request.queryString
        if (queryString != null) {
            reqInfo.append("?").append(queryString)
        }
        if (request.authType != null) {
            reqInfo.append(", authType=")
                    .append(request.authType)
        }
        if (request.userPrincipal != null) {
            reqInfo.append(", principalName=")
                    .append(request.userPrincipal.name)
        }
        logger.debug("=> $reqInfo")

        // ========= Log request and response payload ("body") ========
        // We CANNOT simply read the request payload here, because then the InputStream would be consumed and cannot be read again by the actual processing/server.
        //    String reqBody = DoogiesUtil._stream2String(request.getInputStream());   // THIS WOULD NOT WORK!
        // So we need to apply some stronger magic here :-)
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)
        filterChain.doFilter(wrappedRequest, wrappedResponse) // ======== This performs the actual request!
        val duration = System.currentTimeMillis() - startTime

        // I can only log the request's body AFTER the request has been made and ContentCachingRequestWrapper did its work.
        val requestBody = getContentAsString(wrappedRequest.contentAsByteArray, request.characterEncoding)
        if (requestBody.isNotEmpty()) {
            logger.debug("   Request body:\n$requestBody")
        }
        logger.debug("<= " + reqInfo + ": returned status=" + response.status + " in " + duration + "ms")
        if (includeResponsePayload) {
            val buf = wrappedResponse.contentAsByteArray
            logger.debug("""   Response body: ${getContentAsString(buf, response.characterEncoding)}""")
        }
        wrappedResponse.copyBodyToResponse() // IMPORTANT: copy content of response back into original response
    }
}