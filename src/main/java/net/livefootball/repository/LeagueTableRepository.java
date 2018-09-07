package net.livefootball.repository;

import net.livefootball.model.LeagueTable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueTableRepository extends JpaRepository<LeagueTable,Integer> {

    LeagueTable findByClubId(int clubId);

    boolean existsByClubId(int clubId);

    List<LeagueTable> findAllByClub_League_Id(int leagueId);
}