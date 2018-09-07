package net.livefootball.service.impl;

import lombok.SneakyThrows;
import net.livefootball.pages.MainPage;
import net.livefootball.repository.MatchesRepository;
import net.livefootball.repository.NewsRepository;
import net.livefootball.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class MainServiceImpl implements MainService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private MatchesRepository matchesRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public MainPage getMainPage() {
        return MainPage.builder()
                .matches(matchesRepository.findTop6ByDateGreaterThanOrderByDateDesc(new Date()))
                .match(matchesRepository.findFirstByDateLessThanAndAccountNotInOrderByDateDesc(new Date(),"?"))
                .todayMatches(matchesRepository.findByDateBetween(generateBeforeDate(),generateAfterDate()))
                .newsList(newsRepository.findAll(PageRequest.of(0,4,Sort.Direction.DESC,"createdDate")).getContent())
                .build();
    }


    @SneakyThrows
    static Date generateAfterDate(){
        DATE_FORMAT.applyPattern("yyyy-MM-dd");
        String[] strDate = DATE_FORMAT.format(new Date()).split("-");
        DATE_FORMAT.applyPattern("yyyy-MM-dd HH:mm:ss");
        int month = Integer.parseInt(strDate[2].startsWith("0") ? String.valueOf(strDate[2].charAt(1)) : strDate[2]);
        String strMonth;
        if(month < 10){
            strMonth = "0" + (month + 1);
        }else {
            strMonth = "" + (month + 1);
        }
        return DATE_FORMAT.parse(strDate[0] + "-" + strDate[1] + "-" + strMonth + " 00:00:00");
    }

    @SneakyThrows
    static Date generateBeforeDate(){
        DATE_FORMAT.applyPattern("yyyy-MM-dd");
        String strDate = DATE_FORMAT.format(new Date()).split(" ")[0];
        DATE_FORMAT.applyPattern("yyyy-MM-dd HH:mm:ss");
        return DATE_FORMAT.parse(strDate + " 00:00:00");
    }
}