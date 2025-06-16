package net.ubn.td.package_web.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.ubn.td.package_web.config.PackagerProperties;
import net.ubn.td.package_web.entity.PackagingTask;
import net.ubn.td.package_web.entity.TaskStatus;

/**
 * Scheduled job that processes packaging tasks and cleans up timeouts.
 */
@Component
public class PackagingScheduler {

    private static final Logger log = LoggerFactory.getLogger(PackagingScheduler.class);

    private final PackagingTaskService taskService;
    private final PackagingProcessor processor;
    private final PackagerProperties properties;

    public PackagingScheduler(PackagingTaskService taskService,
                              PackagingProcessor processor,
                              PackagerProperties properties) {
        this.taskService = taskService;
        this.processor = processor;
        this.properties = properties;
    }

    @Scheduled(fixedDelayString = "PT1M")
    public void run() {
        processPendingTasks();
        cleanupTimeouts();
    }

    private void processPendingTasks() {
        List<PackagingTask> pending = taskService.findPending();
        int count = 0;
        for (PackagingTask task : pending) {
            if (count >= properties.getBatchSize()) break;
            processor.process(task);
            count++;
        }
    }

    private void cleanupTimeouts() {
        LocalDateTime limit = LocalDateTime.now().minus(properties.getTimeout());
        for (PackagingTask task : taskService.findProcessingOlderThan(limit)) {
            task.setStatus(TaskStatus.TIMEOUT);
            task.setUpdatedAt(LocalDateTime.now());
            taskService.save(task);
            log.warn("Task {} timed out", task.getId());
        }
    }
}
