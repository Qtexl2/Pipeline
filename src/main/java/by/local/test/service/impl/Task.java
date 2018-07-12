package by.local.test.service.impl;

import by.local.test.dto.ActionDTO;
import by.local.test.dto.Status;
import by.local.test.exception.ApiException;
import by.local.test.model.Pipeline;
import by.local.test.model.TaskEntity;
import by.local.test.repository.PipelineRepository;
import by.local.test.repository.TaskRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static by.local.test.dto.Status.*;

@Data
@ToString(exclude = {"repository", "countDownLatch", "pipelineRepository"})
public class Task {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private PipelineRepository pipelineRepository;
    private TaskEntity taskEntity;
    private CountDownLatch countDownLatch;
    private Set<Task> nextTask;
    private ReentrantLock lock = new ReentrantLock();
    private boolean isExecuted = false;
    private Long pipelineId;


    public void executeTask() throws InterruptedException {
        Pipeline finalPipeline = pipelineRepository.getOne(pipelineId);
        countDownLatch.countDown();
        countDownLatch.await();
        lock.lock();
        if (finalPipeline.getStatus().equals(FAILED) || isExecuted) {
            return;
        }
        finalPipeline.setStatus(IN_PROGRESS);
        pipelineRepository.save(finalPipeline);
        taskEntity.setStatus(IN_PROGRESS);
        taskEntity.setStartTime(new Date());
        repository.save(taskEntity);
        taskEntity = runSubTask(taskEntity);

        switch (taskEntity.getStatus()){
            case FAILED:    finalPipeline.setStatus(FAILED);
                            pipelineRepository.save(finalPipeline);
                            repository.save(taskEntity);
                            break;
            case SKIPPED:
            case COMPLETED: taskEntity.setEndTime(new Date());
                            repository.save(taskEntity);
                            break;
        }
        isExecuted = true;
        lock.unlock();
        if(nextTask.isEmpty()){
            finalPipeline.setStatus(COMPLETED);
            finalPipeline.setEndTime(new Date());
            pipelineRepository.save(finalPipeline);
        }
        for (Task next: nextTask) {
            Thread thread = new Thread(() -> {
                try {
                    next.executeTask();
                } catch (InterruptedException e) {
                    throw new ApiException(ApiException.Message.PIPELINE_EXCEPTION.getText(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
            thread.start();
        }
    }

    private Status getRandom(){
        List<Status> statuses = Arrays.stream(Status.values()).collect(Collectors.toList());
        statuses.remove(PENDING);
        return statuses.get((int)(System.currentTimeMillis() % (Status.values().length-1)));
    }

    private TaskEntity runSubTask(TaskEntity currentTask) throws InterruptedException {
        ActionDTO.Type action = currentTask.getAction();
        switch (action){
            case PRINT:
                System.out.println(currentTask.getName());
                currentTask.setStatus(COMPLETED);
                break;
            case RANDOM:
                System.out.println(currentTask.getName());
                TimeUnit.SECONDS.sleep(1);
                currentTask.setStatus(getRandom());
                break;
            case COMPLITED:
                System.out.println(currentTask.getName());
                TimeUnit.SECONDS.sleep(1);
                currentTask.setStatus(COMPLETED);
                break;
            case DELAYED:
                System.out.println(currentTask.getName());
                TimeUnit.SECONDS.sleep(10);
                currentTask.setStatus(COMPLETED);
                break;
        }
        return currentTask;
    }
}