package net.livefootball.service.impl;

import net.livefootball.model.Club;
import net.livefootball.model.League;
import net.livefootball.model.LeagueTable;
import net.livefootball.pages.ClubPage;
import net.livefootball.repository.ClubRepository;
import net.livefootball.repository.LeagueRepository;
import net.livefootball.repository.LeagueTableRepository;
import net.livefootball.repository.PlayerRepository;
import net.livefootball.service.ClubService;
import net.livefootball.util.FileUtil;
import net.livefootball.util.TableComporatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClubServiceImpl implements ClubService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private LeagueTableRepository leagueTableRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FileUtil fileUtil;

    @Transactional(rollbackFor = Exception.class)
    public void add(Club club, MultipartFile multipartFile) {
        clubRepository.save(club);
        League league = leagueRepository.findById(club.getLeague().getId()).get();
        league.setClubCount(league.getClubCount() + 1);
        String imgUrl = System.currentTimeMillis() + multipartFile.getOriginalFilename();
        try {
            fileUtil.saveImage("clubs\\" + club.getId(),imgUrl,multipartFile);
            club.setImgUrl(club.getId() + "/" + imgUrl);
            LOGGER.debug("club saved");
        }catch (Exception e){
            fileUtil.delete("clubs\\" + club.getId(),true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(int id) {
        return clubRepository.existsById(id);
    }

    @Override
    public Club getByIsChampion(int leagueId) {
        return clubRepository.findByLeagueIdAndChampionTrue(leagueId);
    }

    @Override
    public List<Club> getAll() {
        return clubRepository.findAll();
    }

    @Override
    public List<Club> getAll(Pageable pageable) {
        return clubRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Club> getAllByLeagueId(int leagueId, Pageable pageable) {
        return clubRepository.findAllByLeagueId(leagueId,pageable);
    }

    @Override
    public int count() {
        return (int) clubRepository.count();
    }

    @Override
    public int countByLeagueId(int leagueId) {
        return clubRepository.countByLeagueId(leagueId);
    }

    @Override
    public ClubPage getClubPageById(int id) {
        Club club = clubRepository.findById(id).get();
        List<LeagueTable> tableList = leagueTableRepository.findAllByClub_League_Id(club.getLeague().getId());
        tableList.sort(new TableComporatorImpl());
        return ClubPage.builder()
                .club(club)
                .tableList(tableList)
                .players(playerRepository.findAllByClubId(id,PageRequest.of(0,9)))
                .build();
    }

    @Transactional
    public void deleteById(int id) {
        clubRepository.deleteById(id);
        fileUtil.delete("clubs\\" + id,true);
        LOGGER.debug("club deleted");
    }

    @Override
    public Club getById(int id) {
        return clubRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Club club, MultipartFile multipartFile) {
        clubRepository.save(club);
        if(multipartFile != null){
            try {
                fileUtil.saveImage("clubs",club.getImgUrl(),multipartFile);
                club.setImgUrl(club.getId() + "/" + club.getImgUrl());
                LOGGER.debug("club saved");
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Club getByName(String name) {
        return clubRepository.findByName(name);
    }
}
