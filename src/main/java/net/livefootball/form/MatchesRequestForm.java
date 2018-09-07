package net.livefootball.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchesRequestForm {

    @Range(min = 1,message = "please choose a club")
    private int masterId;

    @Range(min = 1,message = "please choose a club")
    private int guestId;

    @NotNull(message = "invalid date format")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "yyyy-MM-dd")
    private Date date;

    @Length(min = 5,message = "invalid time format")
    private String time;

    @Length(max = 5,message = "in account field wrong data")
    private String account;
}