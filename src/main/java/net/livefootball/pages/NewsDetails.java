package net.livefootball.pages;

import lombok.*;
import net.livefootball.model.League;
import net.livefootball.model.Matches;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDetails extends NewsPage{

    private List<League> leagueList;

    private List<Matches> matchesList;
}