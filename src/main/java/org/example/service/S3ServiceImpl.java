package org.example.service;

import org.example.dao.S3DAO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class S3ServiceImpl implements S3Service {

    private S3DAO s3DAO;

    public S3ServiceImpl(S3DAO s3DAO) {
        this.s3DAO = s3DAO;
    }

    @Override
    public List<Object> getObjectsByBucket(String bucketName) {
        return s3DAO.getObjectsByBucket(bucketName);
    }

    @Override
    public String getObjectContent(String bucketName, String objectKey) {
        InputStream is = s3DAO.getObjectContent(bucketName, objectKey);
        try {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong reading object content");
        }
    }

    @Override
    public String uploadObject(String bucketName, String objectKey, MultipartFile multipartFile) throws IOException {
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());

        try {
            multipartFile.transferTo(tmpFile);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong transferring file content");
        }

        return s3DAO.uploadObject(bucketName, objectKey, tmpFile);
    }
}
