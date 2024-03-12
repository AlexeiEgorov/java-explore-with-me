package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.UserClient;
import ru.practicum.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
public class AdminUserClientController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid UserDto userDto) {
        return client.add(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findUsers(@RequestParam(required = false) List<Long> ids,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10")  @Positive Integer size) {
        return client.findUsers(ids, from, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable @Positive Long id) {
        return client.delete(id);
    }
}
