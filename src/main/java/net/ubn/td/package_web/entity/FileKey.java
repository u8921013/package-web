package net.ubn.td.package_web.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;

/**
 * Stores the encrypted content encryption key for a file.
 */
@Entity
public class FileKey {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    /** Encrypted CEK in Base64 */
    private String encryptedKey;

    public FileKey() {}

    public FileKey(UUID id, FileEntity file, String encryptedKey) {
        this.id = id;
        this.file = file;
        this.encryptedKey = encryptedKey;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }
}
