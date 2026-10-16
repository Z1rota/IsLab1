package org.zirota.islab1.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioFileStorageService {
    private final MinioClient minioClient;
    private final String bucket;
    public MinioFileStorageService(MinioClient minioClient, @Value("${storage.minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    public String upload(InputStream stream, long size) {
        if (size <= 0 ) {
            throw new IllegalArgumentException("Файл пустой");
        }
        String key = "imports/"+ UUID.randomUUID() + ".csv";

        try {
            minioClient.putObject(PutObjectArgs.builder().
                    bucket(bucket).object(key).stream(stream, size, -1).contentType("text/csv").build());
            return key;

        } catch (Exception e) {
            throw new IllegalStateException("Ошибка загрузки файла в ФХ");
        }
    }

    public InputStream download(String key) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(key).build());

        } catch (Exception e) {
            throw new IllegalStateException("Ошибка скачивания файла из ФХ");
        }
    }

    public void delete(String key) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(key).build());
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка удаления файла из ФХ");
        }
    }

}
