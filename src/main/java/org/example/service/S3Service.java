package org.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    List<Object> getObjectsByBucket(String bucketName);

    String getObjectContent(String bucketName, String objectKey);

    String uploadObject(String bucketName, String objectKey, MultipartFile multipartFile) throws IOException;
}
