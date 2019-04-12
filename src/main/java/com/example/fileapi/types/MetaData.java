package com.example.fileapi.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class MetaData {

    private static final Logger log = LoggerFactory.getLogger(MetaData.class);

    private String fileName;
    private String contentType;
    private Long size;

    public List<String> createFileList() {
        return Arrays.asList(
                "fileName=" + fileName,
                "contentType=" + contentType,
                "size=" + size
        );
    }

    public static MetaData createFromFileList(List<String> fileList, String id) {
        String fileName = "";
        String contentType = "";
        Long size = null;

        for(String row : fileList) {

            if(row.startsWith("fileName=")) {
                fileName = row.replace("fileName=", "");
            }

            if(row.startsWith("contentType=")) {
                contentType = row.replace("contentType=", "");
            }

            if(row.startsWith("size")) {

                String stringSize = row.replaceAll("size=", "");

                try {
                    size = Long.valueOf(stringSize);
                } catch (NumberFormatException e) {
                    // This would mean that we have something wrong in the code where we save the meta data,
                    // or that someone has tampered with the saved metadata.
                    log.warn("Unable to parse size value ({}) for file with id ({})", stringSize, id);
                }
            }
        }

        if(fileName.isEmpty()) {
            log.warn("No fileName found in metadata for file with id ({})", id);
        }

        if(contentType.isEmpty()) {
            log.warn("No contentType found in metadata for file with id ({})", id);
        }

        if(size == null) {
            log.warn("No contentType found in metadata for file with id ({})", id);
        }

        return new MetaData(fileName, contentType, size);
    }
}
