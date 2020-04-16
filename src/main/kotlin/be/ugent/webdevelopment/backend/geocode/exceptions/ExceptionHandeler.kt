package be.ugent.webdevelopment.backend.geocode.exceptions

import be.ugent.webdevelopment.backend.geocode.exceptions.wrappers.ExceptionContainerWrapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(ExceptionContainer::class)
    fun handleException(e: ExceptionContainer): ResponseEntity<ExceptionContainerWrapper> {
        return ResponseEntity
                .status(e.code)
                .body(e.wrap())
    }

    @ExceptionHandler(GenericException::class)
    fun handleException(e: GenericException): ResponseEntity<ExceptionContainerWrapper> {
        val ec = ExceptionContainer().also { it.addException(e) }
        return ResponseEntity
                .status(e.code)
                .body(ec.wrap())
    }

    @ExceptionHandler(PropertyException::class)
    fun handleException(e: PropertyException): ResponseEntity<ExceptionContainerWrapper> {
        val ec = ExceptionContainer().also { it.addException(e) }
        return ResponseEntity
                .status(e.code)
                .body(ec.wrap())
    }
}