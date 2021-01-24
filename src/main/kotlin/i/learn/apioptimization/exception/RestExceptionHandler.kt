package i.learn.apioptimization.exception

import i.learn.apioptimization.service.MessageSourceService
import i.learn.apioptimization.support.MessageKey
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class RestExceptionHandler(
    private val messageSourceService: MessageSourceService
) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: Exception, request: WebRequest): ResponseEntity<Any> {
        return handle(
            exception = e,
            request = request,
            status = HttpStatus.NOT_FOUND,
            message = messageSourceService.getMessage(MessageKey.NOT_FOUND_EXCEPTION)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception, request: WebRequest): ResponseEntity<Any> {
        return handle(
            exception = e,
            request = request,
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = messageSourceService.getMessage(MessageKey.EXCEPTION)
        )
    }

    private fun handle(
        exception: Exception,
        request: WebRequest,
        header: HttpHeaders = HttpHeaders.EMPTY,
        status: HttpStatus,
        message: String
    ): ResponseEntity<Any> {
        return handleExceptionInternal(
            exception,
            RestExceptionView(message),
            header,
            status,
            request
        )
    }
}