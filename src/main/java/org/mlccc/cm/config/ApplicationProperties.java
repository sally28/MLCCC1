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
}
