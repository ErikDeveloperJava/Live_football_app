package net.livefootball.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private String trainer;

    private String stadium;

    private String owner;

    private String imgUrl;

    @Column(name = "is_champion")
    private boolean champion;

    @JsonIgnore
    @ManyToOne
    private League league;
}