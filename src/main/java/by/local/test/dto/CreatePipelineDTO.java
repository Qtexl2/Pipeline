package by.local.test.dto;

import by.local.test.util.ProxyHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePipelineDTO {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private List<CreateTaskDTO> tasks;
    @NotNull
    private ProxyHashMap transitions;
}
