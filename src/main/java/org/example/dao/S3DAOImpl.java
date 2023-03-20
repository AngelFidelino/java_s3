package org.example.dao;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class S3DAOImpl implements S3DAO {
    private AmazonS3 amazonS3Client;

    public S3DAOImpl(@Qualifier("S3Client") AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public List<Object> getObjectsByBucket(String bucketName) {
        return amazonS3Client.listObjects(bucketName).getObjectSummaries().stream()
                .map(o -> o.getBucketName() + "/" + o.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public S3ObjectInputStream getObjectContent(String bucketName, String objectKey) {
        if (!amazonS3Client.doesBucketExistV2(bucketName)) {
            throw new NotFoundException("Bucket not found");
        }
        S3Object object = amazonS3Client.getObject(bucketName, objectKey);
        return object.getObjectContent();
    }

    @Override
    public String uploadObject(String bucketName, String objectKey, File file) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, objectKey, file));
        file.delete();
        return "File uploaded";
    }
}
