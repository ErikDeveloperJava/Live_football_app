package net.livefootball.form;

import lombok.*;
import net.livefootball.model.League;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubUpdateRequestForm extends ClubRequestForm {

    private int id;

    private League league;
}