package com.backend.ecommercebackend.entity;

import java.util.Map;
import java.util.Set;

public enum Status {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED;

    private static final Map<Status, Set<Status>> VALID_TRANSITIONS = Map.of(
            PENDING, Set.of(CONFIRMED, CANCELLED),
            CONFIRMED, Set.of(SHIPPED, CANCELLED),
            SHIPPED, Set.of(DELIVERED),
            DELIVERED, Set.of(),
            CANCELLED, Set.of()
    );

    public boolean canTransitionTo(Status newStatus){
        return VALID_TRANSITIONS.get(this).contains(newStatus);
    }
}