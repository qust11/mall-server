package com.ym.user.converter;


import com.ym.common.dto.UserCommonDto;
import com.ym.user.dto.UserDto;
import com.ym.user.entity.User;
import org.mapstruct.Mapper;

/**
 *
 * @author qushutao
 * @since 2026-06-17 14:05
 **/
@Mapper
public interface UserConverterMapper {
    UserConverterMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(UserConverterMapper.class);
    UserDto toUserDto(User user);

    UserCommonDto toUserCommonDto(User one);

    UserCommonDto userDtoToUserCommonDto(UserDto userDto);
}
