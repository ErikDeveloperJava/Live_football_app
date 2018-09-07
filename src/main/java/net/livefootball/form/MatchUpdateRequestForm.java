package net.livefootball.form;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchUpdateRequestForm {

    private int id;

    private String master;

    private String guest;

    private String date;

    private String time;

    private String account;

    private boolean completed;
}