package bepoland.bookconference.config;

import bepoland.bookconference.exception.ResourceNotFoundException;
import bepoland.bookconference.response.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class ApiResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        ApiResponse apiResponse = new ApiResponse(
                false, "Error occurred", ex.getMessage());
        return new ResponseEntity<Object>(
                apiResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
