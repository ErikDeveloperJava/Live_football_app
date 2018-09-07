package net.livefootball.service.impl;

import net.livefootball.model.Club;
import net.livefootball.model.League;
import net.livefootball.model.LeagueTable;
import net.livefootball.model.News;
import net.livefootball.pages.LeaguePage;
import net.livefootball.repository.ClubRepository;
import net.livefootball.repository.LeagueRepository;
import net.livefootball.repository.LeagueTableRepository;
import net.livefootball.repository.NewsRepository;
import net.livefootball.service.LeagueService;
import net.livefootball.util.FileUtil;
import net.livefootball.util.TableComporatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LeagueServiceImpl implements LeagueService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private LeagueTableRepository leagueTableRepository;

    @Override
    public List<League> getAll() {
        return leagueRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(League league, MultipartFile image) {
        leagueRepository.save(league);
        String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
        try {
            fileUtil.saveImage("leagues\\" + league.getId(),imgUrl,image);
            league.setImgUrl(league.getId() + "/" + imgUrl);
            LOGGER.debug("league saved");
        }catch (Exception e){
            fileUtil.delete("leagues\\" + league.getId(),true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(int id) {
        return leagueRepository.existsById(id);
    }

    @Override
    public LeaguePage getLeaguePage(int id) {
        List<Club> clubs = clubRepository.findAllByLeagueId(id,PageRequest.of(0,9));
        List<LeagueTable> leagueTables = new ArrayList<>();
        for (Club club : clubs) {
            LeagueTable table = leagueTableRepository.findByClubId(club.getId());
            if(table != null){
                leagueTables.add(table);
            }
        }
        leagueTables.sort(new TableComporatorImpl());
        return LeaguePage.builder()
                .league(leagueRepository.findById(id).get())
                .clubs(clubs)
                .tableList(leagueTables)
                .build();
    }

    @Override
    public List<League> getAll(Pageable pageable) {
        return leagueRepository.findAll(pageable).getContent();
    }

    @Override
    public int count() {
        return (int) leagueRepository.count();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        List<Club> clubs = clubRepository.findAllByLeagueId(id);
        for (Club club : clubs) {
            fileUtil.delete("clubs\\" + club.getId(),true);
        }
        List<News> newsList = newsRepository.findAllByLeagueId(id);
        for (News news : newsList) {
            if(!news.getVideoUrl().equals("?")){
                fileUtil.delete("" + news.getId(),false);
            }
            fileUtil.delete("news\\" + news.getId(),true);
        }
        leagueRepository.deleteById(id);
        fileUtil.delete("leagues\\"+ id,true);
        LOGGER.debug("league deleted");
    }

    @Override
    public League getById(int id) {
        return leagueRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(League league, MultipartFile multipartFile) {
        leagueRepository.save(league);
        if(multipartFile != null){
            try {
                fileUtil.saveImage("leagues",league.getImgUrl(),multipartFile);
                league.setImgUrl(league.getId() + "/" + league.getImgUrl());
                LOGGER.debug("league saved");
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
