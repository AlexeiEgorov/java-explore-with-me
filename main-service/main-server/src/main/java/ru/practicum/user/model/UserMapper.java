package ru.practicum.user.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.Initiator;
import ru.practicum.user.dto.ResponseUserDto;
import ru.practicum.dto.UserDto;

@UtilityClass
public class UserMapper {
    public ResponseUserDto toResponseDto(User user) {
        return new ResponseUserDto(user.getId(), user.getName(), user.getEmail());
    }

    public Initiator toInitiator(User user) {
        return new Initiator(user.getId(), user.getName());
    }

    public User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}
