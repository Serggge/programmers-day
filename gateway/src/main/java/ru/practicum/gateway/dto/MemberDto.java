package ru.practicum.gateway.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String cohort;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
