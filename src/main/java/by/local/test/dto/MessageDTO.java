package by.local.test.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String message;

    @AllArgsConstructor
    @Getter
    public enum TypeMessage {
        PIPELINE_CREATED("Pipeline with id [%d] was successfully created"),
        PIPELINE_INTERRUPTED("Pipeline with id [%d] was interrupted");
        private String value;
    }
}
