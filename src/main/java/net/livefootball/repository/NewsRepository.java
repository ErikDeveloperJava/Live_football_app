package net.livefootball.repository;

import net.livefootball.model.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Integer> {

    List<News> findAllByOrderByCreatedDateDesc(Pageable pageable);

    List<News> findAllByLeagueId(int leagueId);
}