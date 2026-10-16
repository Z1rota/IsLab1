package org.zirota.islab1.service;


import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zirota.islab1.dto.ImportOperationResponse;
import org.zirota.islab1.entity.AppUser;
import org.zirota.islab1.entity.ImportOperation;
import org.zirota.islab1.entity.ImportStatus;
import org.zirota.islab1.exceptions.NotFoundException;
import org.zirota.islab1.repository.ImportOperationRepository;
import org.zirota.islab1.repository.UserRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ImportHistoryService {

    private final ImportOperationRepository importOperationRepository;
    private final UserRepository userRepository;

    public ImportHistoryService(ImportOperationRepository importOperationRepository, UserRepository userRepository) {
        this.importOperationRepository = importOperationRepository;
        this.userRepository = userRepository;
    }

    public record StartedImport(Long id, String fileKey) {}

    public record FileInfo(String fileKey, String filename) {}

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public StartedImport start(String username, String originalFilename) {

        AppUser user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден"));

        ImportOperation operation = new ImportOperation();

        operation.setUser(user);
        operation.setStatus(ImportStatus.IN_PROGRESS);
        operation.setFileKey("imports/"+ UUID.randomUUID()+".csv");
        importOperationRepository.save(operation);
        return new StartedImport(operation.getId(), operation.getFileKey());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void success(Long id, int count) {
        ImportOperation operation = getForUpdate(id);

        if (operation.getStatus() != ImportStatus.IN_PROGRESS) {
            throw new IllegalStateException("Операция уже завершена");
        }
        operation.setStatus(ImportStatus.SUCCESS);
        operation.setAddedCount(count);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean failedNotCommited(Long id) {
        ImportOperation operation = getForUpdate(id);
        if (operation.getStatus() == ImportStatus.SUCCESS) {
            return false;
        }
        operation.setStatus(ImportStatus.FAILED);
        operation.setAddedCount(null);
        return true;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void fileDeleted(Long id) {
        ImportOperation operation = getForUpdate(id);
        if (operation.getStatus() != ImportStatus.FAILED) {
            throw new IllegalStateException("Нельзя очищать файл успешных импортов");
        }
        operation.setFileKey(null);
    }
    @Transactional(readOnly = true)
    public List<ImportOperationResponse> getUserHistory(String username) {
        return importOperationRepository.findAllByUserUsernameOrderByIdDesc(username).stream().map(op ->
                toResponse(op, false)).toList();
    }

    @Transactional(readOnly = true)
    public List<ImportOperationResponse> getAllHistory() {
        return importOperationRepository.findAllByOrderByIdDesc().stream().map(op -> toResponse(op,false)).toList();
    }

    @Transactional(readOnly = true)
    public FileInfo getFileForDownload(Long id, String username, boolean admin) {
        ImportOperation operation = getOperation(id);
        if (!admin && !operation.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Нет доступа");
        }
        return new FileInfo(operation.getFileKey(), operation.getFileName());
    }
    @Transactional(readOnly = true)
    public List<Long> findStaleImports(ZonedDateTime before) {
        return importOperationRepository.findAllByStatusAndCreatedAtBefore(ImportStatus.IN_PROGRESS, before).stream()
                .map(ImportOperation::getId).toList();
    }

    @Transactional(readOnly = true)
    public List<Long> findFailedImportsWithFiles() {
        return importOperationRepository.findAllByStatusAndFileKeyIsNotNull(ImportStatus.FAILED).stream()
                .map(ImportOperation::getId).toList();
    }

    @Transactional(readOnly = true)
    public String getFailedFileKey(Long id) {
        ImportOperation operation = getOperation(id);

        if (operation.getStatus() != ImportStatus.FAILED) {
            return null;
        }

        return operation.getFileKey();
    }


    private ImportOperation getOperation(Long id) {
        return importOperationRepository.findById(id).orElseThrow(() -> new NotFoundException("Операция не найдена"));
    }
    private ImportOperation getForUpdate(Long id) {
        return importOperationRepository.findForUpdate(id).orElseThrow(() -> new NotFoundException("Операция не найдена"));
    }

    private ImportOperationResponse toResponse(ImportOperation operation, boolean admin) {
        String downloadUrl = null;

        if (operation.getStatus() == ImportStatus.SUCCESS && operation.getFileKey() != null) {
            downloadUrl = admin
                    ? "/api/admin/imports/" + operation.getId() + "/file"
                    : "/api/imports/" + operation.getId() + "/file";
        }

        return new ImportOperationResponse(operation.getId(), operation.getStatus(), operation.getUser().getUsername(),
                operation.getAddedCount(),
                operation.getCreatedAt(),
                operation.getFileName(),
                downloadUrl
        );
    }
    private String sanitizeFileName(String name) {
        if (name == null || name.isBlank()) {
            return "import.csv";
        }

        String normalized = name.replace('\\', '/');
        String result = normalized.substring(normalized.lastIndexOf('/') + 1)
                .replaceAll("[\\r\\n\"]", "_");

        if (result.isBlank()) {
            return "import.csv";
        }

        return result.length() > 200 ? result.substring(result.length() - 200) : result;
    }

}
