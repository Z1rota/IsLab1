package org.zirota.islab1.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportOrchestrator {

    private final ImportHistoryService importHistoryService;
    private final ImportService importService;

    public ImportOrchestrator(ImportHistoryService importHistoryService, ImportService importService) {
        this.importHistoryService = importHistoryService;
        this.importService = importService;
    }

    public void importFile(MultipartFile file, String username) {
        long opId = importHistoryService.start(username);

        try {
            int count = importService.importPerson(file);
            importHistoryService.success(opId, count);

        } catch (RuntimeException e) {
            importHistoryService.failed(opId);
            throw e;
        }
    }
}
