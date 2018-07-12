package by.local.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPipelineDTO {
    private Long executionId;
    private String pipeline;
    private Status status;
    private String startTime;
    private String endTime;
    private List<GetTaskDTO> tasks;
}
