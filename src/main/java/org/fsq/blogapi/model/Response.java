package org.fsq.blogapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {
    private HttpStatus status;
    private String message;
    private Object body;

    public Response(HttpStatus status, Object body) {
        this.status = status;
        this.body = body;
    }

    public Response(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static void buildHttpResponse(HttpStatus status, String msg, HttpServletResponse response) throws IOException {
        Response errResponse = new Response();
        errResponse.setStatus(status);
        errResponse.setMessage(msg);
        ObjectMapper om = new ObjectMapper();
        String reString = om.writeValueAsString(errResponse);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getOutputStream().write(reString.getBytes());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
