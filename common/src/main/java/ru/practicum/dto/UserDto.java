package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserDto {
    private Long id;
    @NotBlank(message = "Name can't be empty")
    @Size(min = 2, max = 250, message = "Name can't have less than 2 or more than 250 symbols")
    private String name;
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email must be valid")
    @Size(min = 6, max = 254, message = "Email can't be less than 6 or more than 254 symbols")
    private String email;
}