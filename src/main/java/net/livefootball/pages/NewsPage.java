package net.livefootball.pages;

import lombok.*;
import net.livefootball.model.News;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsPage {

    private News news;

    private String day;

    private String month;

    private int commentsCount;
}