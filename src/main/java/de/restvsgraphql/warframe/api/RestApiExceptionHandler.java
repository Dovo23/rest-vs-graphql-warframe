package de.restvsgraphql.warframe.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiErrorResponse handleRestClientException() {
        return new ApiErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                "Fissure data is currently unavailable."
        );
    }
}
