package net.ubn.td.package_web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Metadata about an uploaded file stored on disk.
 */
@Entity
public class FileEntity {

    @Id
    private UUID id;

    private String storedName;

    private String originalFilename;

    private LocalDateTime uploadedAt;

    // Optional reference to encrypted key
    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL)
    private FileKey fileKey;

    public FileEntity() {}

    public FileEntity(UUID id, String storedName, String originalFilename, LocalDateTime uploadedAt) {
        this.id = id;
        this.storedName = storedName;
        this.originalFilename = originalFilename;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public FileKey getFileKey() {
        return fileKey;
    }

    public void setFileKey(FileKey fileKey) {
        this.fileKey = fileKey;
    }
}
