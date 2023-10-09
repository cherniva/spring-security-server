package com.cherniva.springsecurityserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private Long id;
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 6, max = 32, message = "Name must be between 6 and 32 characters long")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, max = 32, message = "Name must be between 8 and 32 characters long")
    private String password;
}
