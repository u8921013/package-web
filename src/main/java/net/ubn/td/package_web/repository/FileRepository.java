package net.ubn.td.package_web.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ubn.td.package_web.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
