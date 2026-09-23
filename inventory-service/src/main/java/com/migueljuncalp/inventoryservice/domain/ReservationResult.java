package com.migueljuncalp.inventoryservice.domain;

import com.migueljuncalp.eventcontracts.inventory.v1.ReservationStatus;

import java.util.UUID;

public record ReservationResult(
        UUID orderId,
        ReservationStatus status,
        String reason
) {

    public static ReservationResult reserved(UUID orderId) {
        return new ReservationResult(
                orderId,
                ReservationStatus.RESERVED,
                null
        );
    }

    public static ReservationResult rejected(
            UUID orderId,
            String reason
    ) {
        return new ReservationResult(
                orderId,
                ReservationStatus.REJECTED,
                reason
        );
    }

    public boolean isReserved() {
        return status == ReservationStatus.RESERVED;
    }
}