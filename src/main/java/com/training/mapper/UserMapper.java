package com.training.mapper;

import com.training.dto.user.InputUserDto;
import com.training.dto.user.OutputUserDto;
import com.training.dto.user.UpdatedUserDto;
import com.training.entity.enums.Role;
import com.training.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = Role.class)
public interface UserMapper {

    OutputUserDto convertToDto(User user);

    @Mapping(target = "role", expression = "java(getEmployeeRole())")
    User convertToEntity(InputUserDto inputUserDto);

    User convertUpdatedToEntity(UpdatedUserDto updatedUserDto);

    default Role getEmployeeRole() {
        return Role.EMPLOYEE;
    }

//    List<UserDto> convertListToDto(List<User> users);
//
//    List<User> convertListToEntity(List<UserDto> usersDto);
}
