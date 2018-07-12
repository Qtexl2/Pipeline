package by.local.test.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ITRex Group
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int errorCode;
    private String errorStatus;
}
