package com.example.uploaddownloadservice.uploaddownloadapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class UploadDownloadProperties {

    private String fileDirectory;

}
