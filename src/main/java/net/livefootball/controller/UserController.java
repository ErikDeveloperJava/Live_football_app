package net.livefootball.controller;

import net.livefootball.service.UserService;
import net.livefootball.util.ExcelUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @Autowired
    private ExcelUtil excelUtil;

    @PostMapping("/admin/user/delete")
    public String delete(@RequestParam("userId")int userId){
        LOGGER.debug("userId : {}",userId);
        userService.deleteById(userId);
        return "redirect:/admin";
    }

    @GetMapping("/admin/users/excel")
    public void downloadExcel(HttpServletResponse response){
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
        excelUtil.create(userService.getAll(),response);
    }
}