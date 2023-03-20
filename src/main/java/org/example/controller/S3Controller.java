package org.example.controller;

import org.example.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1.0/s3")
public class S3Controller {

    private S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/{bucketName}")
    public ResponseEntity<?> getBucketContent(
            @PathVariable("bucketName") String bucketName, @RequestParam("objectKey") String objectKey) {
        if (StringUtils.isEmpty(objectKey)) {
            return getBucketObjects(bucketName);
        }
        return new ResponseEntity<String>(
                s3Service.getObjectContent(bucketName, objectKey), HttpStatus.OK);
    }

    private ResponseEntity<List<Object>> getBucketObjects(
            @PathVariable("bucketName") String bucketName) {
        try {
            List<Object> objects = s3Service.getObjectsByBucket(bucketName);
            return new ResponseEntity<>(objects, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return ResponseEntity.of(Optional.of(ex.getMessage())).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{bucketName}")
    public ResponseEntity<String> uploadObject(@PathVariable("bucketName") String bucketName, @RequestParam("objectKey") String objectKey, @RequestParam("file") MultipartFile file) {
        try {
            return new ResponseEntity<>(s3Service.uploadObject(bucketName, objectKey, file), HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.of(Optional.of(e.getMessage())).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
