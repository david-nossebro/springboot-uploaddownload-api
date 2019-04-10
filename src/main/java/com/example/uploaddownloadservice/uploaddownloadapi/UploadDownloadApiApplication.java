package com.example.uploaddownloadservice.uploaddownloadapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		UploadDownloadProperties.class
})
public class UploadDownloadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadDownloadApiApplication.class, args);
	}

}
