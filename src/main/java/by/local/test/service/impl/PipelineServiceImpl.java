package by.local.test.service.impl;

import by.local.test.converter.Converter;
import by.local.test.dto.CreatePipelineDTO;
import by.local.test.dto.Status;
import by.local.test.exception.ApiException;
import by.local.test.model.Pipeline;
import by.local.test.repository.PipelineRepository;
import by.local.test.repository.TaskRepository;
import by.local.test.service.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class PipelineServiceImpl implements PipelineService{
    private final Converter converter;
    private final PipelineRepository pipelineRepository;
    private final PipelineService pipelineService;
    @Autowired
    public PipelineServiceImpl(Converter converter, PipelineRepository pipelineRepository, @Lazy PipelineService pipelineService) {
        this.converter = converter;
        this.pipelineRepository = pipelineRepository;
        this.pipelineService = pipelineService;
    }

    @Override
    public Long createPipeline(CreatePipelineDTO dto) {
        Pipeline pipeline = converter.fromCreatePipelineDTO(dto);
        Pipeline createdPipeline = pipelineRepository.save(pipeline);
        pipelineService.createPipelineAsync(dto,createdPipeline);
        return createdPipeline.getId();
    }

    @Async
    public void createPipelineAsync(CreatePipelineDTO dto, Pipeline createdPipeline){
        HashMap<Task, Set<Task>> map = converter.fromCreatePipelineDTO(dto, createdPipeline);
        String firstTask = dto.getTransitions().getFirstTask();
        for (Map.Entry<Task,Set<Task>> task : map.entrySet()) {
            if (task.getKey().getTaskEntity().getName().equals(firstTask)) {
                try {
                    task.getKey().executeTask();
                } catch (InterruptedException e) {
                    throw new ApiException(ApiException.Message.PIPELINE_EXCEPTION.getText(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    @Override
    public void interruptPipeline(Long id) {
        Optional<Pipeline> pipelineOpt = pipelineRepository.findById(id);
        if(!pipelineOpt.isPresent()){
            throw new ApiException(String.format(ApiException.Message.PIPELINE_NOT_EXIST.getText(),id), HttpStatus.BAD_REQUEST);
        }
        Pipeline pipeline = pipelineOpt.get();
        pipeline.setStatus(Status.FAILED);
        pipelineRepository.save(pipeline);
    }

    @Override
    public Pipeline getPipeline(Long id) {
        Optional<Pipeline> pipeline = pipelineRepository.findById(id);
        if(!pipeline.isPresent()){
            throw new ApiException(String.format(ApiException.Message.PIPELINE_NOT_EXIST.getText(),id), HttpStatus.BAD_REQUEST);
        }
        return pipeline.get();
    }
}
