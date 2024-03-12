package ru.practicum.user.service;

import org.springframework.data.domain.Page;
import ru.practicum.dto.Initiator;
import ru.practicum.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User save(User user);

    User patch(UserDto user, Long id);

    Page<User> findUsers(List<Long> ids, Integer from, Integer size);

    User get(Long id);

    void delete(Long id);

    List<Initiator> findInitiatorsByIds(Set<Long> ids);
}
