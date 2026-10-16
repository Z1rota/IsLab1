package org.zirota.islab1.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zirota.islab1.dto.ImportOperationResponse;
import org.zirota.islab1.service.ImportFileDownloadService;
import org.zirota.islab1.service.ImportHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/imports")
public class AdminImportController {

    private final ImportHistoryService historyService;
    private final ImportFileDownloadService downloadService;
    public AdminImportController(ImportHistoryService historyService, ImportFileDownloadService downloadService) {
        this.historyService = historyService;
        this.downloadService = downloadService;
    }
    @GetMapping("/history")
    public List<ImportOperationResponse> getHistory() {
        return historyService.getAllHistory();
    }
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> downloadFile(@PathVariable long id) {
        return downloadService.download(id,null,true);
    }

}
