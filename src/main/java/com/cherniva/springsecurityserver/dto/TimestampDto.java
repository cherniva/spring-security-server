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
public class TimestampDto {

    private Long id;
    @NotEmpty(message = "Username should not be empty")
    private String timestamp;
}
