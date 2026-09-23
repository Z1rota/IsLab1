package org.zirota.islab1.dto;

import org.zirota.islab1.entity.Role;

public record AuthResponse(String token, String username, Role role) {
}
