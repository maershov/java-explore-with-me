package ru.practicum.user.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(null)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toDtoUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toDtoShortUser(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
