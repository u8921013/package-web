package net.ubn.td.package_web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.ubn.td.package_web.model.FileRecord;
import net.ubn.td.package_web.model.PackageTask;
import net.ubn.td.package_web.service.PackageService;

@RestController
@RequestMapping("/api")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    // 1. file upload
    @PostMapping("/files/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        FileRecord record = packageService.storeFile(file);
        return ResponseEntity.ok(Map.of(
                "fileId", record.getId(),
                "fileName", record.getStoredName()
        ));
    }

    // 2. get original file id by path
    @GetMapping("/files/id")
    public ResponseEntity<?> getFileId(@RequestParam("path") String path) {
        return packageService.getFileByPath(path)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(Map.of("fileId", r.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. list all original files
    @GetMapping("/files")
    public List<FileRecord> listFiles() {
        return packageService.listFiles();
    }

    // 4. request packaging
    @PostMapping("/tasks")
    public ResponseEntity<?> requestTask(@RequestParam("fileId") Long fileId,
                                         @RequestParam("packageType") String packageType) {
        PackageTask task = packageService.requestPackage(fileId, packageType);
        return ResponseEntity.ok(Map.of("taskId", task.getId()));
    }

    // 5. get status by fileId and packageType
    @GetMapping("/status/file/{fileId}")
    public ResponseEntity<?> statusByFile(@PathVariable Long fileId,
                                          @RequestParam("packageType") String packageType) {
        return packageService.getStatusByFile(fileId, packageType)
                .<ResponseEntity<?>>map(t -> ResponseEntity.ok(Map.of(
                        "status", t.getStatus(),
                        "taskId", t.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. get status by taskId
    @GetMapping("/status/task/{taskId}")
    public ResponseEntity<?> statusByTask(@PathVariable Long taskId) {
        return packageService.getStatusByTask(taskId)
                .<ResponseEntity<?>>map(t -> ResponseEntity.ok(Map.of(
                        "status", t.getStatus(),
                        "fileId", t.getFile().getId())))
                .orElse(ResponseEntity.notFound().build());
    }
}
