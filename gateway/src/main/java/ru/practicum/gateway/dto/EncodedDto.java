package ru.practicum.gateway.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EncodedDto {

    private String encoded;
    private Integer offset;
}
