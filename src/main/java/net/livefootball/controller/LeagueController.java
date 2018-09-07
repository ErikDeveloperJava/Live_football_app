package net.livefootball.controller;

import net.livefootball.form.LeagueRequestForm;
import net.livefootball.model.League;
import net.livefootball.service.LeagueService;
import net.livefootball.util.FileUtil;
import net.livefootball.util.PageableUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
public class LeagueController implements Pages {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int PAGE_SIZE = 6;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private PageableUtil pageableUtil;

    @GetMapping("/leagues")
    public @ResponseBody
    List<League> leagues() {
        return leagueService.getAll();
    }

    @GetMapping("/admin/league/add")
    public String addGet(Model model) {
        model.addAttribute("form", new LeagueRequestForm());
        model.addAttribute("action","/admin/league/add");
        return ADD_LEAGUE;
    }

    @PostMapping("/admin/league/add")
    public String addPost(@Valid @ModelAttribute("form") LeagueRequestForm form, BindingResult result) {
        LOGGER.debug("form : {}", form);
        if (result.hasErrors()) {
            return ADD_LEAGUE;
        } else if (form.getImage().isEmpty()) {
            result.addError(new FieldError("form", "image", "image is empty"));
            return ADD_LEAGUE;
        } else if (!fileUtil.isValidImgFormat(form.getImage().getContentType())) {
            result.addError(new FieldError("form", "image", "invalid image format"));
            return ADD_LEAGUE;
        } else {
            League league = League.builder()
                    .name(form.getName())
                    .description(form.getDescription())
                    .clubCount(0)
                    .imgUrl("")
                    .build();
            leagueService.add(league, form.getImage());
            return "redirect:/admin/league";
        }
    }

    @PostMapping("/admin/league/update")
    public String updatePost(@Valid @ModelAttribute("form") LeagueRequestForm form, BindingResult result) {
        MultipartFile multipartFile = null;
        League persistLeague;
        LOGGER.debug("form : {}", form);
        if (result.hasErrors()) {
            return ADD_LEAGUE;
        } else if((persistLeague = leagueService.getById(form.getId())) ==null){
            return ADD_LEAGUE;
        } else {
            if(form.getImage() != null && !form.getImage().isEmpty() && fileUtil.isValidImgFormat(form.getImage().getContentType())){
                multipartFile = form.getImage();
            }
            League league = League.builder()
                    .id(persistLeague.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .clubCount(persistLeague.getClubCount())
                    .imgUrl(persistLeague.getImgUrl())
                    .build();
            leagueService.update(league,multipartFile);
            return "redirect:/admin/league";
        }
    }

    @GetMapping("/league/{id}")
    public String league(@PathVariable("id") String strId, Model model) {
        try {
            if (!leagueService.existsById(Integer.parseInt(strId))) {
                throw new NumberFormatException();
            }
            model.addAttribute("leaguePage", leagueService.getLeaguePage(Integer.parseInt(strId)));
        } catch (NumberFormatException e) {
            return "redirect:/";
        }
        return LEAGUE_DETAILS;
    }

    @GetMapping("/admin/league")
    public String adminLeagues(Pageable pageable, Model model) {
        int length = pageableUtil.getLength(leagueService.count(), PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE), length);
        model.addAttribute("leagueList", leagueService.getAll(pageable));
        model.addAttribute("length", length);
        model.addAttribute("pageNumber", pageable.getPageNumber());
        return LEAGUES;
    }

    @GetMapping("/admin/league/delete/{id}")
    public String delete(@PathVariable("id") String strId) {
        try {
            leagueService.deleteById(Integer.parseInt(strId));
        } catch (NumberFormatException e) {
            //****
        }
        return "redirect:/admin/league";
    }


    @GetMapping("/admin/league/update/{id}")
    public String updateGet(@PathVariable("id")String strId,Model model) {
        League league;
        try {
            if((league = leagueService.getById(Integer.parseInt(strId))) == null){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/admin/league";
        }
        model.addAttribute("form", LeagueRequestForm
                .builder()
                .id(league.getId())
                .name(league.getName())
                .description(league.getDescription())
                .build());
        model.addAttribute("action","/admin/league/update");
        return ADD_LEAGUE;
    }


}