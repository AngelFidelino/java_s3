package org.example.dao;

import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.util.List;

public interface S3DAO {
    List<Object> getObjectsByBucket(String bucketName);

    S3ObjectInputStream getObjectContent(String bucketName, String objectKey);

    String uploadObject(String bucketName, String objectKey, File file);
}
