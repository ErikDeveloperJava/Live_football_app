package net.livefootball.service.impl;

import net.livefootball.model.LeagueTable;
import net.livefootball.repository.LeagueTableRepository;
import net.livefootball.service.LeagueTableService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LeagueTableServiceImpl implements LeagueTableService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private LeagueTableRepository leagueTableRepository;

    @Transactional(rollbackFor = Exception.class)
    public void add(LeagueTable leagueTable) {
        leagueTableRepository.save(leagueTable);
        LOGGER.debug("leagueTable saved");
    }

    @Override
    public boolean existsByClubId(int clubId) {
        return leagueTableRepository.existsByClubId(clubId);
    }
}
