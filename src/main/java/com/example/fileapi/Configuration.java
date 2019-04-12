package com.example.fileapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.PropertySource;

@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class Configuration {

    private String fileStoreDirectory;

}
