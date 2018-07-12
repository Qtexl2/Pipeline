package by.local.test.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    @AllArgsConstructor
    public enum Message{
        INCORRECT_ACTION("ActionDTO incorrect"),
        PIPELINE_EXCEPTION("Pipeline exception"),
        PIPELINE_NOT_EXIST("Pipeline [%s] not exist");
        @Getter
        String text;
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
