package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.InvalidStatusException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        if (userDto.getEmail().length() > 254) {
            throw new InvalidStatusException("Слишком длинная почта");
        }
        Optional<User> optionalUser = userRepository.findByName(userDto.getName());
        if (optionalUser.isPresent()) {
            throw new DataConflictException("Имя уже занято");
        }
        log.info("Добавлен новый пользователь: {}", userDto.getName());
        return UserMapper.toDtoUser(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        int page = 0;
        if (from != 0) {
            page = from / size;
        }
        Pageable pageable = PageRequest.of(page, size);
        if (ids == null) {
            log.info("Получены пользователи со страницы {} размером {}", page, size);
            return userRepository.findAll(pageable).stream().map(UserMapper::toDtoUser).collect(Collectors.toList());
        }
        log.info("Получены пользователи по ID: {}", ids);
        return userRepository.findAllByIdIn(ids, pageable).stream().map(UserMapper::toDtoUser).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userRepository.deleteById(id);
        log.info("Удален пользователь с ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return UserMapper.toDtoUser(userRepository.findById(id).orElseThrow(() -> {
            log.error("Ошибка при получении пользователя по ID: Пользователь с ID {} не найден", id);
            return new UserNotFoundException("Пользователь не найден");
        }));
    }
}
