package net.livefootball.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master")
    private Club master;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest")
    private Club guest;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String account;
}