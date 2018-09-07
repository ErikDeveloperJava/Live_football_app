package net.livefootball.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int played;

    private int won;

    private int drawn;

    private int lost;

    private int points;

    @ManyToOne
    private Club club;
}