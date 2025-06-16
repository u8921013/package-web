package net.ubn.td.package_web.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.ubn.td.package_web.entity.FileEntity;
import net.ubn.td.package_web.entity.PackageType;
import net.ubn.td.package_web.entity.PackagingTask;
import net.ubn.td.package_web.entity.TaskStatus;
import net.ubn.td.package_web.repository.PackagingTaskRepository;
import net.ubn.td.package_web.util.UuidUtils;

@Service
@Transactional
public class PackagingTaskService {

    private final PackagingTaskRepository taskRepository;

    public PackagingTaskService(PackagingTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public PackagingTask createTask(FileEntity file, PackageType type) {
        PackagingTask task = new PackagingTask(UuidUtils.newUuid(), file, type, TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    public List<PackagingTask> findPending() {
        return taskRepository.findByStatusOrderByCreatedAtAsc(TaskStatus.PENDING);
    }

    public List<PackagingTask> findProcessingOlderThan(LocalDateTime limit) {
        return taskRepository.findByStatusAndUpdatedAtBefore(TaskStatus.PROCESSING, limit);
    }

    public Optional<PackagingTask> get(UUID id) {
        return taskRepository.findById(id);
    }

    public PackagingTask save(PackagingTask task) {
        return taskRepository.save(task);
    }
}
