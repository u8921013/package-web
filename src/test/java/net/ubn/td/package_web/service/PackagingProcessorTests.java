import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import net.ubn.td.package_web.entity.FileEntity;
import net.ubn.td.package_web.entity.PackageType;
import net.ubn.td.package_web.entity.PackagingTask;
import net.ubn.td.package_web.entity.TaskStatus;
import net.ubn.td.package_web.repository.FileKeyRepository;
import net.ubn.td.package_web.repository.FileRepository;
import net.ubn.td.package_web.service.PackagingProcessor;
import net.ubn.td.package_web.service.PackagingTaskService;
import net.ubn.td.package_web.service.RemoteApiService;
import net.ubn.td.package_web.util.UuidUtils;

@SpringBootTest
@Transactional
class PackagingProcessorTests {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PackagingTaskService taskService;

    @Autowired
    private PackagingProcessor processor;

    @Autowired
    private FileKeyRepository keyRepository;

    @MockBean
    private RemoteApiService remoteApiService;

    private final Path packageDir = Path.of("packages");

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(packageDir);
    }

    @AfterEach
    void cleanup() throws IOException {
        FileSystemUtils.deleteRecursively(packageDir);
    }

    @Test
    void processCreatesPackageAndUpdatesTask() {
        FileEntity file = new FileEntity(UuidUtils.newUuid(), "stored.txt", "original.txt", LocalDateTime.now());
        fileRepository.save(file);
        PackagingTask task = taskService.createTask(file, PackageType.EPUB_LCP);

        processor.process(task);

        PackagingTask updated = taskService.get(task.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(updated.getOutputPath()).isNotBlank();

        assertTrue(Files.exists(Path.of(updated.getOutputPath())));
        assertTrue(keyRepository.findById(task.getId()).isPresent());

        verify(remoteApiService).sendMetadata(anyMap());
    }
}
