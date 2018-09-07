package net.livefootball.repository;

import net.livefootball.model.Club;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Integer> {

    Club findByLeagueIdAndChampionTrue(int leagueId);

    List<Club> findAllByLeagueId(int leagueId, Pageable pageable);

    int countByLeagueId(int leagueId);

    List<Club> findAllByLeagueId(int leagueId);

    Club findByName(String name);
}