package net.livefootball.pages;

import lombok.*;
import net.livefootball.model.Matches;
import net.livefootball.model.News;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPage {

    private List<Matches> matches;

    private List<Matches> todayMatches;

    private Matches match;

    private List<News> newsList;
}