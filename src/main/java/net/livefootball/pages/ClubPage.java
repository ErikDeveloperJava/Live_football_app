package net.livefootball.pages;

import lombok.*;
import net.livefootball.model.Club;
import net.livefootball.model.LeagueTable;
import net.livefootball.model.Player;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubPage {

    private Club club;

    private List<Player> players;

    private List<LeagueTable> tableList;
}