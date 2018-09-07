package net.livefootball.controller;

import net.livefootball.model.User;
import net.livefootball.model.UserRole;
import net.livefootball.service.MainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class MainController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private MainService mainService;

    @GetMapping("/")
    public String main(@RequestAttribute("user") User user, Model model) {
        if (user.getRole().equals(UserRole.ADMIN)) {
            LOGGER.debug("user role ADMIN redirect '/admin' url");
            return "redirect:/admin";
        } else {
            model.addAttribute("mainPage",mainService.getMainPage());
            return INDEX;
        }
    }
}