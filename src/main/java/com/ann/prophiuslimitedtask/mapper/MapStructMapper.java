package com.ann.prophiuslimitedtask.mapper;

import com.ann.prophiuslimitedtask.DTO.UpdateUserInfoDto;
import com.ann.prophiuslimitedtask.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
@Component
public interface MapStructMapper {
    User userUpdateDtoToUser(UpdateUserInfoDto updateUserInfoDto);
}
