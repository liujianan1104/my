package com.dinfo.dispatcher.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangxf
 */
@Configuration
@ConfigurationProperties(prefix = "dinfo.dispatcher.library")
public class LibraryProperties {

    private String path = "./";

    private String tempPath = "/temp";

    public String getPath() {
        return path;
    }

    public LibraryProperties setPath(String path) {
        this.path = path;
        return this;
    }

    public String getTempPath() {
        return tempPath;
    }

    public LibraryProperties setTempPath(String tempPath) {
        this.tempPath = tempPath;
        return this;
    }
}
