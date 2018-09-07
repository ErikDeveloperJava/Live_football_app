package net.livefootball.repository;

import net.livefootball.model.Club;
import net.livefootball.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByIdNotIn(int userId, Pageable pageable);

    int countByIdNotIn(int userId);

}