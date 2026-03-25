package com.nhiennhatt.bookstoreapi.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {
    private final Logger logger = LoggerFactory.getLogger(MinioService.class);

    @Value("${minio.bucket-name}")
    private String bucket;

    @Autowired
    private MinioClient minioClient;

    @PostConstruct
    public void createBucketIfNotExists() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public void uploadFile(MultipartFile file, String fileName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .object(fileName)
                .build()
        );
    }

    public void deleteFile(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(fileName)
                .build()
        );
    }

    public String getPresignedUrl(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .expiry(30, TimeUnit.MINUTES)
                .object(fileName)
                .method(Method.GET)
                .build()
        );
    }

    public void deletePattern(String pattern) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(pattern)
                        .recursive(true)
                        .build()
        );

        List<DeleteObject> objects = new LinkedList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            objects.add(new DeleteObject(item.objectName()));
        }
        logger.info("Deleting objects: {}", objects);

        if (!objects.isEmpty()) {
            Iterable<Result<DeleteError>> deleteErrors = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucket)
                            .objects(objects)
                            .build()
            );

            for (Result<DeleteError> result : deleteErrors) {
                DeleteError error = result.get();
                logger.error("Error deleting object: {}", error);
            }
        }
    }
}
