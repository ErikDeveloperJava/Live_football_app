package net.livefootball.service;

import net.livefootball.model.Matches;
import net.livefootball.pages.MatchPage;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface MatchesService {

    List<Matches> getTop3();

    void add(Matches matches);

    List<MatchPage> geAllByDateGreaterThan(Pageable pageable);

    List<MatchPage> geAllByDateLessThan(Pageable pageable);

    List<MatchPage> getAllByDateBetween(Pageable pageable);

    int countByDateBetween();

    int countByDateGreaterThan(Date date);

    int countByDateLessThan(Date date);

    int count();

    List<MatchPage> getAll(Pageable pageable);

    Matches getById(int id);

    void deleteById(int id);
}