package net.livefootball.form;

import lombok.*;
import net.livefootball.model.League;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpdateRequestForm extends NewsRequestForm{

    private int id;

    private League league;
}