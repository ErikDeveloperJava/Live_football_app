package net.livefootball.repository;

import net.livefootball.model.Matches;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MatchesRepository extends JpaRepository<Matches,Integer> {

    List<Matches> findTop6ByDateGreaterThanOrderByDateDesc(Date date);

    Matches findFirstByDateLessThanAndAccountNotInOrderByDateDesc(Date date,String account);

    List<Matches> findByDateBetween(Date before,Date after);

    List<Matches> findByDateBetween(Date before,Date after,Pageable pageable);

    int countByDateBetween(Date before,Date after);

    int countByDateGreaterThan(Date date);

    List<Matches> findAllByDateGreaterThan(Date date, Pageable pageable);

    List<Matches> findAllByDateLessThanAndAccountNotIn(Date date,String param,Pageable pageable);

    int  countByDateLessThanAndAccountNotIn(Date date,String param);

    List<Matches> findAllByDateLessThan(Date date,Pageable pageable);

    int countByDateLessThan(Date date);
}