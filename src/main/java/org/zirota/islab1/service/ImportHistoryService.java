package org.zirota.islab1.service;


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

import java.util.List;

@Service
public class ImportHistoryService {
    private final ImportOperationRepository importOperationRepository;
    private final UserRepository userRepository;
    public ImportHistoryService(ImportOperationRepository importOperationRepository, UserRepository userRepository) {
        this.importOperationRepository = importOperationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long start(String username) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден"));
        ImportOperation operation = new ImportOperation();
        operation.setUser(user);
        operation.setStatus(ImportStatus.IN_PROGRESS);
        return importOperationRepository.save(operation).getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void success(Long id, int count) {
        ImportOperation operation = getOperation(id);

        operation.setStatus(ImportStatus.SUCCESS);
        operation.setAddedCount(count);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failed(Long id) {
        ImportOperation operation = getOperation(id);
        operation.setStatus(ImportStatus.FAILED);
        operation.setAddedCount(null);
    }

    @Transactional(readOnly = true)
    public List<ImportOperationResponse> getUserHistory(String username) {
        return importOperationRepository.findAllByUserUsernameOrderByIdDesc(username)
                .stream().map(this::toResponse).toList();
    }
    @Transactional(readOnly = true)
    public List<ImportOperationResponse> getAllHistory() {
        return importOperationRepository.finadAllByOrderByIdDesc().stream().map(this::toResponse).toList();
    }

    private ImportOperation getOperation(Long id) {
        return importOperationRepository.findById(id).orElseThrow(() -> new NotFoundException("Операция не найдена"));
    }
    private ImportOperationResponse toResponse(ImportOperation operation) {
        return new ImportOperationResponse(operation.getId(),operation.getStatus(),operation.getUser().getUsername(),
                operation.getAddedCount(),operation.getCreatedAt());
    }

}
