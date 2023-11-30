package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addNewUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void removeUser(Long id);

    UserDto getUserById(Long id);
}
