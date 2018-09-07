package net.livefootball.repository;

import net.livefootball.model.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Integer> {

    List<Player> findAllByClubId(int clubId, Pageable pageable);

    int countByClubId(int clubId);
}