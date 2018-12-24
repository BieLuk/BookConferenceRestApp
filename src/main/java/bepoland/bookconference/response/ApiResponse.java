package bepoland.bookconference.response;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ApiResponse {
    private Boolean success;
    private String message;
    private List<String> errors;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, String message, List<String> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

    public ApiResponse(Boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
