package com.training.dto.user;

import com.training.dto.role.OutputRoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OutputUserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private OutputRoleDto role;
    private String email;
}
