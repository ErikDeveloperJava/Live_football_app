package net.livefootball.form;

import lombok.*;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseForm {

    private List<FieldError> errors;

    private boolean success;
}