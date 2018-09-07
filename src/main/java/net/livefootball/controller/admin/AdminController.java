package net.livefootball.controller.admin;

import net.livefootball.controller.Pages;
import net.livefootball.model.User;
import net.livefootball.repository.UserRepository;
import net.livefootball.service.UserService;
import net.livefootball.util.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class AdminController implements Pages {

    @Autowired
    private UserService userService;

    @Autowired
    private PageableUtil pageableUtil;

    @GetMapping("/admin")
    public String main(Model model, @RequestAttribute("user")User user,
                       Pageable pageable){
        int length = pageableUtil.getLength(userService.countByIdNotIn(user.getId()),pageable.getPageSize());
        pageable = pageableUtil.getCheckedPageable(pageable,length);
        model.addAttribute("users",userService.getAllByIdNotIn(pageable,user.getId()));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return "admin/" + INDEX;
    }
}