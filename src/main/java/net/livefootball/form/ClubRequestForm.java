package net.livefootball.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubRequestForm {

    @Length(min = 2,max = 255,message = "in name field wrong data")
    private String name;

    @Length(min = 8,message = "in description field wrong data")
    private String description;

    @Length(min = 2,max = 255,message = "in trainer field wrong data")
    private String trainer;

    @Length(min = 2,max = 255,message = "in stadium field wrong data")
    private String stadium;

    @Length(min = 2,max = 255,message = "in owner field wrong data")
    private String owner;

    @Length(min = 1,max = 255,message = "please choose Yes or No")
    private String champion;

    private int leagueId;

    private MultipartFile image;
}