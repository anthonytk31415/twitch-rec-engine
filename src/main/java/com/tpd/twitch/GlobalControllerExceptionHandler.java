package com.tpd.twitch;

import com.tpd.twitch.model.TwitchErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

// ==》 These pieces of information are written for Spring Boot to use. When Spring Boot is ready to use them, it can refer to this content to execute the following code.
@ControllerAdvice
// Every time it runs, it needs to come here for advice. If there's an error thrown and I don't want to use default handling, I'll handle it using the following approach.
public class GlobalControllerExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);


    // If the exception class is not found, use the most general exception handler for all other exceptions other than the specific ones specified below.
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<TwitchErrorResponse> handleDefaultException(Exception e) {
        logger.error("", e);
        return new ResponseEntity<>(
                new TwitchErrorResponse("Something went wrong, please try again later.",
                        e.getClass().getName(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Add one more exception - this means if there is such an exception, give me a chance to catch it and tell me specifically what the exception is.
    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<TwitchErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        logger.error("", e.getCause());
        return new ResponseEntity<>(
                // TwitchErrorResponse is our own organized error response - where did the error occur? Why did it occur?
                new TwitchErrorResponse(e.getReason(), e.getCause().getClass().getName(), e.getCause().getMessage()),
                e.getStatusCode()
        );
    }

    // If needed, other exceptions can also be added here.
}
