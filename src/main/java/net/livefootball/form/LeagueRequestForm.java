package net.livefootball.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueRequestForm {

    private int id;

    @Length(min = 2,max = 255,message = "in name field wrong data")
    private String name;

    @Length(min = 10,message = "in description field wrong data")
    private String description;

    MultipartFile image;
}