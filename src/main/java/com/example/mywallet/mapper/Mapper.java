package com.example.mywallet.mapper;

import com.example.mywallet.DTO.Receipt;
import com.example.mywallet.DTO.TokenDto;
import com.example.mywallet.DTO.UserDto;
import com.example.mywallet.entities.Token;
import com.example.mywallet.entities.User;
import com.example.mywallet.entities.Wallet;
import org.mapstruct.Mapping;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {


    UserDto USER_DTO(User user);

    User Dto_To_User(UserDto userDto);

    TokenDto TOKEN_DTO(Token Token);

    List<UserDto> USER_DTO_LIST(List<User> users);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "balance", target = "currentBalance")
    @Mapping(source = "accountId", target = "accountId")
    Receipt walletToReceipt(Wallet wallet);



}
