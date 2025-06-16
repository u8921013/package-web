package net.ubn.td.package_web.controller;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.ubn.td.package_web.entity.FileEntity;
import net.ubn.td.package_web.service.FileService;

/** REST endpoints related to files. */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /** Uploads a file and returns a fileId. */
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        FileEntity entity = fileService.store(file);
        return ResponseEntity.ok(Map.of("fileId", entity.getId()));
    }
}
