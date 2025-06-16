package net.ubn.td.package_web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.ubn.td.package_web.model.PackageTask;

public interface PackageTaskRepository extends JpaRepository<PackageTask, Long> {
    Optional<PackageTask> findFirstByFileIdAndPackageType(Long fileId, String packageType);
    List<PackageTask> findByFileId(Long fileId);
}
