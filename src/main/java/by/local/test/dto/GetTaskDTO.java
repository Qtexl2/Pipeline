package by.local.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTaskDTO {
    private String name;
    private Status status;
    private String startTime;
    private String endTime;
}
