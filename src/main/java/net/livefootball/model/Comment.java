package net.livefootball.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private News news;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
}