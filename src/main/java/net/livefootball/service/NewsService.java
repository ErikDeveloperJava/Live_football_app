package net.livefootball.service;

import net.livefootball.model.News;
import net.livefootball.pages.NewsDetails;
import net.livefootball.pages.NewsPage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsService {

    void add(News news, MultipartFile image,MultipartFile video);

    List<NewsPage> getNewsPage(Pageable pageable);

    int count();

    NewsDetails getDetailsById(int id);

    boolean existsById(int id);

    List<News> getAll(Pageable pageable);

    void deleteById(int id);

    News getById(int id);

    void update(News news,MultipartFile image,MultipartFile video);
}