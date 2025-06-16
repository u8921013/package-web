package net.ubn.td.package_web.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties loaded from Nacos.
 */
@ConfigurationProperties(prefix = "packager")
public class PackagerProperties {

    /** Max tasks processed per schedule run. */
    private int batchSize = 5;

    /** Timeout for processing tasks. */
    private Duration timeout = Duration.ofMinutes(30);

    private String remoteApiUrl;
    private String tokenUrl;
    private String clientId;
    private String clientSecret;
    private String masterKey;

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public String getRemoteApiUrl() {
        return remoteApiUrl;
    }

    public void setRemoteApiUrl(String remoteApiUrl) {
        this.remoteApiUrl = remoteApiUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }
}
