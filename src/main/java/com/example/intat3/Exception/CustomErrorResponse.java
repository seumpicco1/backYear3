package com.example.intat3.Exception;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter@Setter
public class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String detail;
    private String Message;
    private String path;

    public CustomErrorResponse(LocalDateTime timestamp, int status, String error, String Message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.detail = error;
        this.path = path;
        this.Message = Message;
    }
}
