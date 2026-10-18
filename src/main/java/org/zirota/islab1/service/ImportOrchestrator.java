package org.zirota.islab1.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(ImportOrchestrator.class);

    private final ImportHistoryService historyService;
    private final ImportService importService;
    private final MinioFileStorageService fileStorage;

    @Value("${app.import.test.fail-after-minio:false}")
    private boolean failAfterMinio;

    @Value("${app.import.test.pause-after-minio-ms:0}")
    private long pauseAfterMinioMs;

    public ImportOrchestrator(ImportHistoryService importHistoryService, ImportService importService,
                              MinioFileStorageService fileStorage) {
        this.historyService = importHistoryService;
        this.importService = importService;
        this.fileStorage = fileStorage;
    }

    public void importFile(MultipartFile file, String username) {

        var operation = historyService.start(username, file.getOriginalFilename());
        Long operationId = operation.id();
        String fileKey = operation.fileKey();

        try {
            fileStorage.upload(file, fileKey);

            if (pauseAfterMinioMs > 0) {
                log.info("TEST: Import {} paused for {} ms", operationId, pauseAfterMinioMs);

                try {
                    Thread.sleep(pauseAfterMinioMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Пауза импорта прервана", e);
                }
            }

            if (failAfterMinio) {
                throw new RuntimeException("TEST: RuntimeException после загрузки в MinIO");
            }
            importService.importPerson(file, operationId);

            log.info("Import {} committed successfully", operationId);

        } catch (RuntimeException originalError) {
            log.error(
                    "Import {} failed, exception={}",
                    operationId,
                    originalError.getClass().getName(),
                    originalError
            );

            try {
                boolean failed = historyService.failedNotCommited(operationId);

                if (!failed) {
                    log.warn("Import {} already committed", operationId, originalError);
                    return;
                }
                try {
                    fileStorage.delete(fileKey);
                    historyService.fileDeleted(operationId);

                } catch (RuntimeException cleanupError) {
                    log.error("Cleanup pending for import {}", operationId, cleanupError);
                    originalError.addSuppressed(cleanupError);
                }

            } catch (RuntimeException statusError) {
                log.error("Cannot determine status of import {}", operationId, statusError);
                originalError.addSuppressed(statusError);
            }
            throw originalError;
        }
    }
}
