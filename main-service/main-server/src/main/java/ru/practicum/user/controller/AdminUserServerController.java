package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.ResponseUserDto;
import ru.practicum.dto.UserDto;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserServerController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto add(@RequestBody UserDto userDto) {
        return UserMapper.toResponseDto(userService.save(UserMapper.toUser(userDto)));
    }

    @GetMapping
    public Collection<ResponseUserDto> findUsers(@RequestParam(required = false) List<Long> ids,
                                                 @RequestParam Integer from,
                                                 @RequestParam Integer size) {
        return userService.findUsers(ids, from, size).stream().map(UserMapper::toResponseDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
