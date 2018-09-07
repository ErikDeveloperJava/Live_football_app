package net.livefootball.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueTableRequestForm {

    @Range(message = "in played field wrong data")
    private int played;

    @Range(message = "in won field wrong data")
    private int won;

    @Range(message = "in drawn field wrong data")
    private int drawn;

    @Range(message = "in lost field wrong data")
    private int lost;

    @Range(message = "in points field wrong data")
    private int points;

    @Range(min = 1,message = "please choose a club")
    private int clubId;
}