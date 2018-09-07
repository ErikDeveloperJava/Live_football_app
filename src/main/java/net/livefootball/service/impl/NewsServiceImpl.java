package net.livefootball.service.impl;

import net.livefootball.model.News;
import net.livefootball.pages.NewsDetails;
import net.livefootball.pages.NewsPage;
import net.livefootball.repository.CommentRepository;
import net.livefootball.repository.LeagueRepository;
import net.livefootball.repository.MatchesRepository;
import net.livefootball.repository.NewsRepository;
import net.livefootball.service.MainService;
import net.livefootball.service.NewsService;
import net.livefootball.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private MatchesRepository matchesRepository;

    @Transactional(rollbackFor = Exception.class)
    public void add(News news, MultipartFile image, MultipartFile video) {
        newsRepository.save(news);
        String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
        String videoUrl = "";
        if(!video.isEmpty()){
            videoUrl = System.currentTimeMillis() + "video.mp4";
        }
        try {
            fileUtil.saveImage("news\\" + news.getId(),imgUrl,image);
            news.setImgUrl(news.getId() + "/" + imgUrl);
            if(!video.isEmpty()){
                fileUtil.saveVideo(String.valueOf(news.getId()),videoUrl,video);
                news.setVideoUrl(news.getId() + "/" + videoUrl);
            }
            LOGGER.debug("news saved");
        }catch (Exception e){
            if(!video.isEmpty()){
                fileUtil.delete(String.valueOf(news.getId()),false);
            }
            fileUtil.delete("news\\" + news.getId(),true);
            throw  new RuntimeException(e);
        }
    }

    @Override
    public List<NewsPage> getNewsPage(Pageable pageable) {
        List<News> newsList = newsRepository.findAllByOrderByCreatedDateDesc(pageable);
        List<NewsPage> newsPageList = new ArrayList<>();
        for (News news : newsList) {
            newsPageList.add(NewsPage.builder()
                    .day(DATE_FORMAT.format(news.getCreatedDate()).split("-")[2])
                    .month(MatchesServiceImpl.formatMonth(news.getCreatedDate()))
                    .news(news)
                    .commentsCount(commentRepository.countByNewsId(news.getId()))
                    .build());
        }
        return newsPageList;
    }

    @Override
    public int count() {
        return (int)newsRepository.count();
    }

    @Override
    public NewsDetails getDetailsById(int id) {
        NewsDetails newsDetails = new NewsDetails();
        newsDetails.setNews(newsRepository.findById(id).get());
        newsDetails.setDay(DATE_FORMAT.format(newsDetails.getNews().getCreatedDate()).split("-")[2]);
        newsDetails.setMonth(MatchesServiceImpl.formatMonth(newsDetails.getNews().getCreatedDate()));
        newsDetails.setCommentsCount(commentRepository.countByNewsId(id));
        newsDetails.setLeagueList(leagueRepository.findAll());
        newsDetails.setMatchesList(matchesRepository.findAllByDateGreaterThan(MainServiceImpl.generateBeforeDate(),PageRequest.of(0,3)));
        return newsDetails;
    }

    @Override
    public boolean existsById(int id) {
        return newsRepository.existsById(id);
    }

    @Override
    public List<News> getAll(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedDateDesc(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        Optional<News> news = newsRepository.findById(id);
        if(!news.isPresent()){
            throw new NumberFormatException();
        }
        if(!news.get().getVideoUrl().equals("?")){
            fileUtil.delete("" + id,false);
        }
        fileUtil.delete("news\\" + id,true);
        newsRepository.deleteById(id);
        LOGGER.debug("news deleted");
    }

    @Override
    public News getById(int id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(News news, MultipartFile image, MultipartFile video) {
        newsRepository.save(news);
        if(image != null){
            fileUtil.saveImage("news",news.getImgUrl(),image);
        }
        if(video != null){
            String[] strVideoUrl = news.getVideoUrl().split("/");
            fileUtil.saveVideo(strVideoUrl[0],strVideoUrl[1],video);
        }
        LOGGER.debug("news saved");
    }
}