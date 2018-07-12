package by.local.test.converter;

import by.local.test.config.AppConfig;
import by.local.test.dto.*;
import by.local.test.model.Pipeline;
import by.local.test.model.TaskEntity;
import by.local.test.repository.TaskRepository;
import by.local.test.service.impl.Task;
import by.local.test.util.ProxyHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Component
public class Converter {
    private final AppConfig appConfig;
    private final TaskRepository repository;
    private SimpleDateFormat dateFormat;
    @Autowired
    public Converter(AppConfig appConfig, TaskRepository repository) {
        this.appConfig = appConfig;
        this.repository = repository;
        dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    }

    public Pipeline fromCreatePipelineDTO(CreatePipelineDTO dto){
        return Pipeline.builder()
                .name(dto.getName())
                .startTime(new Date())
                .status(Status.PENDING)
                .description(dto.getDescription())
                .isBreak(false)
                .build();
    }

    public HashMap<Task,Set<Task>> fromCreatePipelineDTO(CreatePipelineDTO dto, Pipeline pipeline){
        List<CreateTaskDTO> tasks = dto.getTasks();
        HashMap<Task, Set<Task>> outMap = new HashMap<>();
        for (CreateTaskDTO taskDTO: tasks) {
            Task task = appConfig.getTask();
            task.setPipelineId(pipeline.getId());
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setPipeline(pipeline);
            taskEntity.setDescription(taskDTO.getDescription());
            taskEntity.setName(taskDTO.getName());
            taskEntity.setAction(taskDTO.getAction().getType());
            taskEntity.setStatus(Status.PENDING);
            taskEntity = repository.save(taskEntity);
            task.setTaskEntity(taskEntity);
            outMap.put(task,null);
        }

        ProxyHashMap transitions = dto.getTransitions();
        for (Map.Entry<Task, Set<Task>> task: outMap.entrySet()) {
            Task currentTask = task.getKey();
            ProxyHashMap.Entity entity = transitions.getEntity(task.getKey().getTaskEntity().getName());
            currentTask.setCountDownLatch(new CountDownLatch(entity.getCount()));
            Set<Task> nextTask = new HashSet<>();
            if(entity.getSet() != null){
                for (TaskEntity tempEntity: entity.getSet()) {
                    for (Task taskKey: outMap.keySet()) {
                        if(tempEntity.getName().equalsIgnoreCase(taskKey.getTaskEntity().getName())){
                            nextTask.add(taskKey);
                        }
                    }
                }
            }
            currentTask.setNextTask(nextTask);
        }
        return outMap;
    }

    public GetTaskDTO toGetTaskDTO(TaskEntity task){
        return GetTaskDTO.builder()
                .endTime(task.getEndTime() != null?dateFormat.format(task.getEndTime()):null)
                .startTime(task.getStartTime() != null?dateFormat.format(task.getStartTime()):null)
                .name(task.getName())
                .status(task.getStatus())
                .build();
    }
    public GetPipelineDTO toGetPipelineDTO(Pipeline pipeline){
        return GetPipelineDTO.builder()
                .executionId(pipeline.getId())
                .pipeline(pipeline.getName())
                .startTime(pipeline.getStartTime() != null?dateFormat.format(pipeline.getStartTime()):null)
                .endTime(pipeline.getEndTime() != null?dateFormat.format(pipeline.getEndTime()):null)
                .status(pipeline.getStatus())
                .tasks(pipeline.getTasks().stream()
                        .sorted(Comparator.comparing(TaskEntity::getId))
                        .map(this::toGetTaskDTO)
                        .collect(Collectors.toList()))
                .build();
    }

}
