package net.ubn.td.package_web.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.ubn.td.package_web.config.PackagerProperties;
import net.ubn.td.package_web.entity.FileEntity;
import net.ubn.td.package_web.entity.FileKey;
import net.ubn.td.package_web.entity.PackageType;
import net.ubn.td.package_web.entity.PackagingTask;
import net.ubn.td.package_web.entity.TaskStatus;
import net.ubn.td.package_web.repository.FileKeyRepository;
import net.ubn.td.package_web.util.CekUtils;

/**
 * Handles the actual packaging logic (stubbed).
 */
@Service
public class PackagingProcessor {

    private static final Logger log = LoggerFactory.getLogger(PackagingProcessor.class);

    private final FileKeyRepository keyRepository;
    private final RemoteApiService remoteApiService;
    private final PackagerProperties properties;
    private final PackagingTaskService taskService;

    public PackagingProcessor(FileKeyRepository keyRepository,
                              RemoteApiService remoteApiService,
                              PackagerProperties properties,
                              PackagingTaskService taskService) {
        this.keyRepository = keyRepository;
        this.remoteApiService = remoteApiService;
        this.properties = properties;
        this.taskService = taskService;
    }

    /**
     * Process a single packaging task.
     */
    public void process(PackagingTask task) {
        try {
            task.setStatus(TaskStatus.PROCESSING);
            task.setUpdatedAt(LocalDateTime.now());
            taskService.save(task);

            FileEntity file = task.getFile();
            String encryptedCek = CekUtils.generateEncryptedCek(properties.getMasterKey());
            FileKey key = new FileKey(task.getId(), file, encryptedCek);
            keyRepository.save(key);

            // TODO real packaging based on task.getPackageType()
            Path packaged = Path.of("packages", file.getId() + "_" + task.getPackageType() + ".pkg");
            Files.createDirectories(packaged.getParent());
            Files.writeString(packaged, "stub" + System.lineSeparator());

            task.setOutputPath(packaged.toString());
            task.setStatus(TaskStatus.COMPLETED);
            task.setUpdatedAt(LocalDateTime.now());
            taskService.save(task);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("taskId", task.getId().toString());
            metadata.put("fileId", file.getId().toString());
            metadata.put("packageType", task.getPackageType().name());
            remoteApiService.sendMetadata(metadata);

            log.info("Task {} completed", task.getId());
        } catch (IOException e) {
            log.error("Failed to package task {}", task.getId(), e);
            task.setStatus(TaskStatus.FAILED);
            task.setUpdatedAt(LocalDateTime.now());
            taskService.save(task);
        }
    }
}
