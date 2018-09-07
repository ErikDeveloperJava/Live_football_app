package net.livefootball.service;

import net.livefootball.model.Club;
import net.livefootball.pages.ClubPage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClubService {

    void add(Club club, MultipartFile multipartFile);

    boolean existsById(int id);

    Club getByIsChampion(int leagueId);

    List<Club> getAll();

    List<Club> getAll(Pageable pageable);

    List<Club> getAllByLeagueId(int leagueId,Pageable pageable);

    int count();

    int countByLeagueId(int leagueId);

    ClubPage getClubPageById(int id);

    void deleteById(int id);

    Club getById(int id);

    void update(Club club,MultipartFile multipartFile);

    Club getByName(String name);
}