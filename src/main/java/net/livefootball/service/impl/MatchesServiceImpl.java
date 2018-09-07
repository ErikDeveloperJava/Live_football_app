package net.livefootball.service.impl;

import net.livefootball.model.Matches;
import net.livefootball.pages.MatchPage;
import net.livefootball.repository.MatchesRepository;
import net.livefootball.service.MainService;
import net.livefootball.service.MatchesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MatchesServiceImpl implements MatchesService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MatchesRepository matchesRepository;

    @Override
    public List<Matches> getTop3() {
        return matchesRepository.findTop6ByDateGreaterThanOrderByDateDesc(new Date());
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(Matches matches) {
        matchesRepository.save(matches);
        LOGGER.debug("match saved");
    }

    @Override
    public List<MatchPage> geAllByDateGreaterThan(Pageable pageable) {
        List<Matches> matches = matchesRepository.findAllByDateGreaterThan(new Date(),pageable);
        List<MatchPage> matchPageList = new ArrayList<>();
        for (Matches match : matches) {

            matchPageList.add(MatchPage.builder()
                    .day(Integer.parseInt(DATE_FORMAT.format(match.getDate()).split("-")[2]))
                    .matches(match)
                    .month(formatMonth(match.getDate()))
                    .build());
        }
        return matchPageList;
    }

    @Override
    public List<MatchPage> geAllByDateLessThan(Pageable pageable) {
        List<Matches> matches = matchesRepository.findAllByDateLessThan(new Date(),pageable);
        List<MatchPage> matchPageList = new ArrayList<>();
        for (Matches match : matches) {
            matchPageList.add(MatchPage.builder()
                    .day(Integer.parseInt(DATE_FORMAT.format(match.getDate()).split("-")[2]))
                    .matches(match)
                    .month(formatMonth(match.getDate()))
                    .build());
        }
        return matchPageList;
    }

    @Override
    public List<MatchPage> getAllByDateBetween(Pageable pageable) {
        List<Matches> matches = matchesRepository
                .findByDateBetween(MainServiceImpl.generateBeforeDate(),MainServiceImpl.generateAfterDate(),pageable);
        List<MatchPage> matchPageList = new ArrayList<>();
        for (Matches match : matches) {

            matchPageList.add(MatchPage.builder()
                    .day(Integer.parseInt(DATE_FORMAT.format(match.getDate()).split("-")[2]))
                    .matches(match)
                    .month(formatMonth(match.getDate()))
                    .build());
        }
        return matchPageList;
    }

    @Override
    public int countByDateBetween() {
        return matchesRepository.countByDateBetween(MainServiceImpl.generateBeforeDate(),MainServiceImpl.generateAfterDate());
    }

    static String formatMonth(Date date){
        String[]strDate = DATE_FORMAT.format(date).split("-");
        String month = "";
        switch (strDate[1]){
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "Jun";
                break;
            case "07":
                month = "Jul";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
        }
        return month;
    }

    @Override
    public int countByDateGreaterThan(Date date) {
        return matchesRepository.countByDateGreaterThan(new Date());
    }

    @Override
    public int countByDateLessThan(Date date) {
        return matchesRepository.countByDateLessThan(date);
    }

    @Override
    public int count() {
        return (int)matchesRepository.count();
    }

    @Override
    public List<MatchPage> getAll(Pageable pageable) {
        List<Matches> matches = matchesRepository
                .findAll(pageable).getContent();
        List<MatchPage> matchPageList = new ArrayList<>();
        for (Matches match : matches) {

            matchPageList.add(MatchPage.builder()
                    .day(Integer.parseInt(DATE_FORMAT.format(match.getDate()).split("-")[2]))
                    .matches(match)
                    .month(formatMonth(match.getDate()))
                    .build());
        }
        return matchPageList;
    }

    @Override
    public Matches getById(int id) {
        return matchesRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(int id) {
        matchesRepository.deleteById(id);
        LOGGER.debug("match deleted");
    }

}
