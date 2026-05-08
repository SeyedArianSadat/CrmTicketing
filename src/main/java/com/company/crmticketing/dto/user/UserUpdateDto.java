package com.company.crmticketing.dto.user;

import jakarta.validation.constraints.*;

public record UserUpdateDto(
    @Size(min = 2, max = 50) String firstName,
    @Size(min = 2, max = 50) String lastName,
    @Email String email,
    @Pattern(regexp = "^[+]?[0-9]{10,15}$") String phoneNumber,
    String profilePictureUrl
) {}
