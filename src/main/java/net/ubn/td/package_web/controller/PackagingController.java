package net.ubn.td.package_web.controller;

import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ubn.td.package_web.entity.PackageType;
import net.ubn.td.package_web.entity.PackagingTask;
import net.ubn.td.package_web.service.FileService;
import net.ubn.td.package_web.service.PackagingTaskService;

/** Endpoints to request packaging and query status. */
@RestController
@RequestMapping("/api/package")
public class PackagingController {

    private final PackagingTaskService taskService;
    private final FileService fileService;

    public PackagingController(PackagingTaskService taskService, FileService fileService) {
        this.taskService = taskService;
        this.fileService = fileService;
    }

    /** Create a packaging task for the given file. */
    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestParam("fileId") UUID fileId,
                                     @RequestParam("type") PackageType type) {
        PackagingTask task = taskService.createTask(fileService.get(fileId), type);
        return ResponseEntity.ok(Map.of("taskId", task.getId()));
    }

    /** Query task status. */
    @GetMapping("/status/{taskId}")
    public ResponseEntity<?> status(@PathVariable UUID taskId) {
        return taskService.get(taskId)
                .<ResponseEntity<?>>map(t -> ResponseEntity.ok(Map.of(
                        "status", t.getStatus(),
                        "outputPath", t.getOutputPath())) )
                .orElse(ResponseEntity.notFound().build());
    }
}
