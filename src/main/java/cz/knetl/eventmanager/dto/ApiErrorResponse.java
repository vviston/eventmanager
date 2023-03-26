package cz.knetl.eventmanager.dto;

/**
 *  The ApiErrorResponse represents error response in REST API
 * */
public class ApiErrorResponse{


    private String status = "ERROR";
    private String message;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
