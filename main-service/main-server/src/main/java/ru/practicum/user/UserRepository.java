package ru.practicum.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.Initiator;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from users where id in (?1)",
            countQuery = "select count(*) from users where id in (?1)", nativeQuery = true)
    Page<User> findUsersByIds(List<Long> ids, Pageable pageable);

    @Query("select new ru.practicum.dto.Initiator(u.id, u.name) from User u")
    List<Initiator> findInitiatorsByIds(Set<Long> ids);
}
