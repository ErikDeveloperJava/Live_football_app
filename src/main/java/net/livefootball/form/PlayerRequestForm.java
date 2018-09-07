package net.livefootball.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Past;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRequestForm {

    @Length(min = 2,max = 255,message = "in name filed wrong data")
    private String name;

    @Length(min = 2,max = 255,message = "in surname filed wrong data")
    private String surname;

    @Length(min = 2,max = 255,message = "in nationality filed wrong data")
    private String nationality;

    @Past(message = "invalid birth date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Length(min = 4,max = 255,message = "in posiotion field wrong data")
    private String position;

    private int clubId;

    private MultipartFile image;
}