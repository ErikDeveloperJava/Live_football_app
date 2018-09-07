package net.livefootball.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOrAdminForm extends UserRequestForm {

    @Length(min = 4,max = 5,message = "invalid role")
    private String role;
}