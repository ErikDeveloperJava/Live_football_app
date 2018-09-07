package net.livefootball.form;

import lombok.*;
import net.livefootball.model.Club;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerUpdateRequestForm extends PlayerRequestForm{

    private int id;

    private Club club;
}