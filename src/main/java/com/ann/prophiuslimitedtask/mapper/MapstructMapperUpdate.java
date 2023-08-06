package com.ann.prophiuslimitedtask.mapper;

import com.ann.prophiuslimitedtask.DTO.UpdateUserInfoDto;
import com.ann.prophiuslimitedtask.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapstructMapperUpdate {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "country", ignore = true)
    void updateUserFromUserUpdateDto(UpdateUserInfoDto updateUserInfoDto, @MappingTarget User user);
}
