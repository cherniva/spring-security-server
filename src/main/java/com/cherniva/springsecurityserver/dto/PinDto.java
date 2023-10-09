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
public class PinDto
{
    private Long id;
    @NotEmpty(message = "Value should not be empty")
    private String value;
    @NotEmpty(message = "Value should not be empty")
    @Size(min = 6, max = 6, message = "Value must be 6 characters long")
    private String password;

}
