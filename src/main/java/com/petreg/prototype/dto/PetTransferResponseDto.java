package com.petreg.prototype.dto;

import java.time.LocalDateTime;

import com.petreg.prototype.model.type.TransferStatus;

public record PetTransferResponseDto(Long id, Long petId, Long oldOwnerId, Long newOwnerId,
        TransferStatus status, LocalDateTime startedAt, LocalDateTime confirmedAt) {
}
