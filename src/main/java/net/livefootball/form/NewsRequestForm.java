package net.livefootball.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsRequestForm {

    @Length(min = 2,max = 255,message = "in name field wrong data")
    private String title;

    @Length(min = 8,message = "in description field wrong data")
    private String description;

    private int leagueId;

    private MultipartFile image;

    private MultipartFile video;
}