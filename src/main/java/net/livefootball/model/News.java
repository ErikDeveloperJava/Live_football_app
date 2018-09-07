package net.livefootball.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String imgUrl;

    private String videoUrl;

    @JsonIgnore
    @ManyToOne
    private League league;
}