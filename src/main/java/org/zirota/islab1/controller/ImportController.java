package org.zirota.islab1.controller;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zirota.islab1.dto.ImportOperationResponse;
import org.zirota.islab1.dto.PersonImportRow;
import org.zirota.islab1.service.ImportHistoryService;
import org.zirota.islab1.service.ImportOrchestrator;
import org.zirota.islab1.service.ImportService;

import java.util.List;

@RestController
@RequestMapping("/api/imports")
public class ImportController {
    private final ImportHistoryService historyService;
    private final ImportOrchestrator importOrchestrator;
    public ImportController(ImportHistoryService historyService, ImportOrchestrator importOrchestrator) {
        this.historyService = historyService;
        this.importOrchestrator = importOrchestrator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void importPersons(@RequestParam("file")MultipartFile file, Authentication authentication) {
        importOrchestrator.importFile(file,authentication.getName());
    }

    @GetMapping("/history")
    public List<ImportOperationResponse> getHistory(Authentication authentication) {
        return historyService.getUserHistory(authentication.getName());
    }

}
