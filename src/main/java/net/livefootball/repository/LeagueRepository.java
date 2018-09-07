package net.livefootball.repository;

import net.livefootball.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League,Integer> {
}