package ru.xromza.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.xromza.user.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"individualDetails", "legalDetails"})
    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findAllByIdsIn(List<Long> ids);

    boolean existsByLogin(String login);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByIsApproved(Pageable pageable, Boolean isApproved);
}
