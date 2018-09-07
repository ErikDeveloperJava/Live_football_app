package net.livefootball.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String nationality;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    private int age;

    private String position;

    private String imgUrl;

    @ManyToOne
    private Club club;
}