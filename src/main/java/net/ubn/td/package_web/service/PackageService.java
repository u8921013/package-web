package net.ubn.td.package_web.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import net.ubn.td.package_web.model.FileRecord;
import net.ubn.td.package_web.model.PackageTask;
import net.ubn.td.package_web.repository.FileRecordRepository;
import net.ubn.td.package_web.repository.PackageTaskRepository;

@Service
@Transactional
public class PackageService {

    private final FileRecordRepository fileRecordRepository;
    private final PackageTaskRepository packageTaskRepository;

    private final Path uploadDir = Path.of("uploads");

    public PackageService(FileRecordRepository fileRecordRepository,
                          PackageTaskRepository packageTaskRepository) throws IOException {
        this.fileRecordRepository = fileRecordRepository;
        this.packageTaskRepository = packageTaskRepository;
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public FileRecord storeFile(MultipartFile file) throws IOException {
        String storedName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path target = uploadDir.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        FileRecord record = new FileRecord(storedName, target.toString());
        return fileRecordRepository.save(record);
    }

    public Optional<FileRecord> getFileByPath(String path) {
        return fileRecordRepository.findByOriginalPath(path);
    }

    public List<FileRecord> listFiles() {
        return fileRecordRepository.findAll();
    }

    public PackageTask requestPackage(Long fileId, String packageType) {
        FileRecord file = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));
        PackageTask task = new PackageTask(file, packageType, "PROCESSING");
        return packageTaskRepository.save(task);
    }

    public Optional<PackageTask> getStatusByFile(Long fileId, String packageType) {
        return packageTaskRepository.findFirstByFileIdAndPackageType(fileId, packageType);
    }

    public Optional<PackageTask> getStatusByTask(Long taskId) {
        return packageTaskRepository.findById(taskId);
    }
}
