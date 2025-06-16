package net.ubn.td.package_web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.ubn.td.package_web.model.FileRecord;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    Optional<FileRecord> findByOriginalPath(String originalPath);
}
