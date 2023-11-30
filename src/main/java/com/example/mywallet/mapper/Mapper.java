package com.example.mywallet.mapper;

import com.example.mywallet.DTO.TokenDto;
import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.ConfirmationToken;
import com.example.mywallet.entities.User;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {


    UserDto USER_DTO(User user);

    User Dto_To_User(UserDto userDto);

    TokenDto TOKEN_DTO(ConfirmationToken Token);

    List<UserDto> USER_DTO_LIST(List<User> users);



}
