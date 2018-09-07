package net.livefootball.service;

import net.livefootball.model.League;
import net.livefootball.pages.LeaguePage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LeagueService {

    List<League> getAll();

    void add(League league, MultipartFile image);

    boolean existsById(int id);

    LeaguePage getLeaguePage(int id);

    List<League> getAll(Pageable pageable);

    int count();

    void deleteById(int id);

    League getById(int id);

    void update(League league,MultipartFile multipartFile);
}