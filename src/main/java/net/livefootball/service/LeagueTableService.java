package net.livefootball.service;

import net.livefootball.model.LeagueTable;
import org.springframework.stereotype.Service;

public interface LeagueTableService {

    void add(LeagueTable leagueTable);

    boolean existsByClubId(int clubId);
}