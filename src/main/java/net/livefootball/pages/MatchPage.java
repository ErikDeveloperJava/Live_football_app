package net.livefootball.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.livefootball.model.Matches;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchPage {

    private Matches matches;

    private int day;

    private String month;
}