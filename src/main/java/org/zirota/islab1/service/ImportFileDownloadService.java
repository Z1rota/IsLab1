package org.zirota.islab1.service;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ImportFileDownloadService {
    private final ImportHistoryService historyService;
    private final MinioFileStorageService storage;

    public ImportFileDownloadService(ImportHistoryService historyService, MinioFileStorageService storage) {
        this.historyService = historyService;
        this.storage = storage;
    }

    public ResponseEntity<byte[]> download(Long id, String username, boolean admin) {
        var info = historyService.getFileForDownload(id, username, admin);

        try (InputStream input = storage.download(info.fileKey())) {
            byte[] content = input.readAllBytes();

            String disposition = ContentDisposition.attachment().filename(info.filename(), StandardCharsets.UTF_8)
                    .build().toString();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,disposition)
                    .contentType(MediaType.parseMediaType("text/csv")).body(content);
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка чтения CSV из Minio");
        }

    }
}
