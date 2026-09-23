package org.zirota.islab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message = "Логин не должен быть пустым") String username,
                              @NotBlank(message = "Пароль не должен быть пустым")
                              @Size(min = 6,message = "Пароль должен содержать не менее 6 символов ") String password) {
}
