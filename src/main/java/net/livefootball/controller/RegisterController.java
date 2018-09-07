package net.livefootball.controller;

import net.livefootball.form.UserOrAdminForm;
import net.livefootball.form.UserRequestForm;
import net.livefootball.form.UserResponseForm;
import net.livefootball.model.User;
import net.livefootball.model.UserRole;
import net.livefootball.service.UserService;
import net.livefootball.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
public class RegisterController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public @ResponseBody
    UserResponseForm register(@Valid UserRequestForm form, BindingResult result){
        LOGGER.debug("form : {}",form);
        if(result.hasErrors()){
            return UserResponseForm.builder()
                    .errors(result.getFieldErrors())
                    .build();
        }else if(userService.existsByUsername(form.getUsername())){
            return UserResponseForm.builder()
                    .errors(Arrays.asList(new FieldError("form","username",
                            "user with username '" + form.getUsername() + "' already exists")))
                    .build();
        } else if(form.getImage().isEmpty()){
            return UserResponseForm.builder()
                    .errors(Arrays.asList(new FieldError("form","image","image is empty")))
                    .build();
        }else if(!fileUtil.isValidImgFormat(form.getImage().getContentType())){
            return UserResponseForm.builder()
                    .errors(Arrays.asList(new FieldError("form","image","invalid image format")))
                    .build();
        }else {
            User user = User.builder()
                    .name(form.getName())
                    .username(form.getUsername())
                    .password(form.getPassword())
                    .imgUrl("")
                    .role(UserRole.USER)
                    .build();
            userService.add(user,form.getImage());
            return UserResponseForm.builder()
                    .success(true)
                    .build();
        }
    }

    @GetMapping("/admin/user/add")
    public String adminRegisterGet(Model model){
        model.addAttribute("form",new UserOrAdminForm());
        return USER_REGISTER;
    }

    @PostMapping("/admin/user/add")
    public String adminRegisterPost(@Valid @ModelAttribute("form")UserOrAdminForm form,BindingResult result){
        if(result.hasErrors()){
            return USER_REGISTER;
        }else if(userService.existsByUsername(form.getUsername())){
            result.addError(new FieldError("form","username",
                    "user with username '" + form.getUsername() +"' already exists"));
            return USER_REGISTER;
        }
        UserRole userRole;
        try {
            userRole = UserRole.valueOf(form.getRole());
        }catch (Exception e){
            result.addError(new FieldError("form","role",
                    "invalid role"));
            return USER_REGISTER;
        }
        if(form.getImage().isEmpty()){
            result.addError(new FieldError("form","image",
                    "image is empty"));
            return USER_REGISTER;
        }else if(!fileUtil.isValidImgFormat(form.getImage().getContentType())){
            result.addError(new FieldError("form","image",
                    "invalid image format"));
            return USER_REGISTER;
        }else {

            User user = User.builder()
                    .name(form.getName())
                    .username(form.getUsername())
                    .password(form.getPassword())
                    .imgUrl("")
                    .role(userRole)
                    .build();
            userService.add(user,form.getImage());
            return "redirect:/admin";
        }
    }
}