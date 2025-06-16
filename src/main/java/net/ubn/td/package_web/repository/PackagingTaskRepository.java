package net.ubn.td.package_web.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ubn.td.package_web.entity.PackagingTask;
import net.ubn.td.package_web.entity.TaskStatus;

public interface PackagingTaskRepository extends JpaRepository<PackagingTask, UUID> {
    List<PackagingTask> findByStatusOrderByCreatedAtAsc(TaskStatus status);
    List<PackagingTask> findByStatus(TaskStatus status);
    List<PackagingTask> findByStatusAndUpdatedAtBefore(TaskStatus status, LocalDateTime time);
}
