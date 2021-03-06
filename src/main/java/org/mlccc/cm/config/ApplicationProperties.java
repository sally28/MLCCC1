package org.mlccc.cm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private String newsletterUploadDir;
    private String mediaUploadDir;
    private String uploadedFileDir;
    private String keyFilePath;
    private String ivFilePath;
    private String anEnvironment;
    private String anLoginId;
    private String anTransactionKey;
    private String anKey;


    public String getNewsletterUploadDir() {
        return newsletterUploadDir;
    }

    public void setNewsletterUploadDir(String newsletterUploadDir) {
        this.newsletterUploadDir = newsletterUploadDir;
    }

    public String getUploadedFileDir() {
        return uploadedFileDir;
    }

    public void setUploadedFileDir(String uploadedFileDir) {
        this.uploadedFileDir = uploadedFileDir;
    }

    public String getMediaUploadDir() {
        return mediaUploadDir;
    }

    public void setMediaUploadDir(String mediaUploadDir) {
        this.mediaUploadDir = mediaUploadDir;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public String getIvFilePath() {
        return ivFilePath;
    }

    public void setIvFilePath(String ivFilePath) {
        this.ivFilePath = ivFilePath;
    }

    public String getAnLoginId() {
        return anLoginId;
    }

    public void setAnLoginId(String anLoginId) {
        this.anLoginId = anLoginId;
    }

    public String getAnTransactionKey() {
        return anTransactionKey;
    }

    public void setAnTransactionKey(String anTransactionKey) {
        this.anTransactionKey = anTransactionKey;
    }

    public String getAnKey() {
        return anKey;
    }

    public void setAnKey(String anKey) {
        this.anKey = anKey;
    }

    public String getAnEnvironment() {
        return anEnvironment;
    }

    public void setAnEnvironment(String anEnvironment) {
        this.anEnvironment = anEnvironment;
    }
}
