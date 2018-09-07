package net.livefootball.form;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestForm {

    @Length(min = 2,max = 255,message = "in name field wrong data")
    private String name;

    @Length(min = 2,max = 255,message = "in username field wrong data")
    private String username;

    @Length(min = 4,max = 255,message = "in password field wrong data")
    private String password;

    private MultipartFile image;
}