package net.ubn.td.package_web.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import net.ubn.td.package_web.entity.FileEntity;
import net.ubn.td.package_web.repository.FileRepository;
import net.ubn.td.package_web.util.UuidUtils;

/** Service for storing uploaded files. */
@Service
@Transactional
public class FileService {

    private final FileRepository fileRepository;
    private final Path uploadDir = Path.of("uploads");

    public FileService(FileRepository fileRepository) throws IOException {
        this.fileRepository = fileRepository;
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public FileEntity store(MultipartFile file) throws IOException {
        UUID id = UuidUtils.newUuid();
        String storedName = id + "_" + file.getOriginalFilename();
        Path target = uploadDir.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        FileEntity entity = new FileEntity(id, storedName, file.getOriginalFilename(), LocalDateTime.now());
        return fileRepository.save(entity);
    }

    public FileEntity get(UUID id) {
        return fileRepository.findById(id).orElseThrow();
    }
}
