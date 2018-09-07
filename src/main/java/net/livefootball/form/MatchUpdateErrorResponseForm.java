package net.livefootball.form;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchUpdateErrorResponseForm {

    private boolean masterError;

    private boolean guestError;

    private boolean dateError;

    private boolean timeError;

    private boolean accountError;
}