package org.zirota.islab1.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zirota.islab1.dto.ImportOperationResponse;
import org.zirota.islab1.service.ImportHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/imports")
public class AdminImportController {

    private final ImportHistoryService historyService;
    public AdminImportController(ImportHistoryService historyService) {
        this.historyService = historyService;
    }
    @GetMapping("/history")
    public List<ImportOperationResponse> getHistory() {
        return historyService.getAllHistory();
    }

}
