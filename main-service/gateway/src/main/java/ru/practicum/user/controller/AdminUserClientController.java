package ru.practicum.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.model.ConstraintViolationException;
import ru.practicum.user.UserClient;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.Constants.POSITIVE_ID_CONS;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users")
public class AdminUserClientController {
    private final UserClient client;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid UserDto userDto) {
        ResponseEntity<Object> responseEntity = client.add(userDto);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            UserDto addedUserDto = objectMapper.convertValue(responseEntity.getBody(), UserDto.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedUserDto);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        }
    }

    @GetMapping
    public ResponseEntity<Object> findUsers(@RequestParam(required = false) List<Long> ids,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return client.findUsers(ids, from, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (id < 1) {
            throw new ConstraintViolationException(POSITIVE_ID_CONS);
        }
        return client.delete(id);
    }
}
