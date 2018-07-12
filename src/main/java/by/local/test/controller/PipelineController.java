package by.local.test.controller;

import by.local.test.converter.Converter;
import by.local.test.dto.CreatePipelineDTO;
import by.local.test.dto.GetPipelineDTO;
import by.local.test.dto.MessageDTO;
import by.local.test.model.Pipeline;
import by.local.test.service.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {

    private final PipelineService pipelineService;
    private final Converter converter;

    @Autowired
    public PipelineController(PipelineService pipelineService, Converter converter) {
        this.pipelineService = pipelineService;
        this.converter = converter;
    }

    @PostMapping(consumes = "text/yaml")
    public MessageDTO createPipeline(@RequestBody @Valid CreatePipelineDTO dto){
        Long id = pipelineService.createPipeline(dto);
        return new MessageDTO(String.format(MessageDTO.TypeMessage.PIPELINE_CREATED.getValue(),id));
    }

    @GetMapping(value = "/{id}", produces = "text/yaml")
    public GetPipelineDTO getPipeline(@PathVariable Long id){
        Pipeline pipeline = pipelineService.getPipeline(id);
        return converter.toGetPipelineDTO(pipeline);
    }

    @PutMapping(value = "/{id}")
    public MessageDTO interruptPipeline(@PathVariable Long id){
        pipelineService.interruptPipeline(id);
        return new MessageDTO(String.format(MessageDTO.TypeMessage.PIPELINE_INTERRUPTED.getValue(),id));
    }
}
