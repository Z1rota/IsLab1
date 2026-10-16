package org.zirota.islab1.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public void upload(MultipartFile file, String key) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл пуст");
        }

        try (InputStream input = file.getInputStream()) {

            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(key)
                    .stream(input, file.getSize(), -1).contentType("text/csv").build());

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Не удалось сохранить CSV в MinIO", e);
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
