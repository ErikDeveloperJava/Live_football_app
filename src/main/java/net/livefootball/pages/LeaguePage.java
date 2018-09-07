package net.livefootball.pages;

import lombok.*;
import net.livefootball.model.Club;
import net.livefootball.model.League;
import net.livefootball.model.LeagueTable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaguePage {

    private League league;

    private List<Club> clubs;

    private List<LeagueTable> tableList;
}