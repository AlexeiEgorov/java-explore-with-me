package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.Initiator;
import ru.practicum.exception.EntityNotFoundException;
import ru.practicum.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.UserRepository;

import java.util.List;
import java.util.Set;

import static ru.practicum.LocalConstants.USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public User patch(UserDto user, Long id) {
        User patched = get(id);
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            patched.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            patched.setName(user.getName());
        }
        return repository.save(patched);
    }

    @Override
    public Page<User> findUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return ids == null ? repository.findAll(pageRequest) : repository.findUsersByIds(ids, pageRequest);
    }

    @Override
    public User get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER, id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        get(id);
        repository.deleteById(id);
    }

    @Override
    public List<Initiator> findInitiatorsByIds(Set<Long> ids) {
        return repository.findInitiatorsByIds(ids);
    }
}
