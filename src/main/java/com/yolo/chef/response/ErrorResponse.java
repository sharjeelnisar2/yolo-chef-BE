package com.yolo.chef.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String details;
    public ErrorResponse(String message,String details)
    {
        this.message=message;
        this.details=details;
    }
}
