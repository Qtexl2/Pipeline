package by.local.test.service;

import by.local.test.dto.CreatePipelineDTO;
import by.local.test.model.Pipeline;


public interface PipelineService {

    Long createPipeline(CreatePipelineDTO dto);

    Pipeline getPipeline(Long id);

    void createPipelineAsync(CreatePipelineDTO dto, Pipeline createdPipeline);

    void interruptPipeline(Long id);
}
