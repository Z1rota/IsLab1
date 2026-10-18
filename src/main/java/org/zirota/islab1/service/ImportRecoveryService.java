package org.zirota.islab1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class ImportRecoveryService {

    private static final Logger log = LoggerFactory.getLogger(ImportRecoveryService.class);
    private final ImportHistoryService historyService;
    private final MinioFileStorageService storage;

    public ImportRecoveryService(ImportHistoryService importHistoryService,
                                 MinioFileStorageService minioFileStorageService) {
        this.historyService = importHistoryService;
        this.storage = minioFileStorageService;
    }

    @Value("${app.import.stale-after-seconds:1800}")
    private long staleAfterSeconds;

    @Scheduled(fixedDelayString = "${app.import.recovery-interval-ms}",
            initialDelayString = "${app.import.recovery-interval-ms}" )
    public void recover() {
        try {
            var stale = historyService.findStaleImports(ZonedDateTime.now().minusSeconds(staleAfterSeconds));
            for (Long id: stale) {
                try {
                    historyService.failedNotCommited(id);
                } catch (RuntimeException e) {
                    log.error("Cannot recover import {}",id, e);
                }
            }
            var failed = historyService.findFailedImportsWithFiles();
            for (Long id: failed) {
                try {
                    String key = historyService.getFailedFileKey(id);

                    if (key == null) {
                        continue;
                    }
                    storage.delete(key);
                    historyService.fileDeleted(id);
                    log.info("recovered import {}",id);
                } catch (RuntimeException e) {
                    log.error("cleanup failed for import {}",id, e);
                }
            }
        } catch (RuntimeException e) {
            log.error("Import recovery failed", e);
        }
    }
}
