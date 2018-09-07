package net.livefootball.controller;

import net.livefootball.form.LeagueTableRequestForm;
import net.livefootball.model.Club;
import net.livefootball.model.LeagueTable;
import net.livefootball.service.ClubService;
import net.livefootball.service.LeagueTableService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LeagueTableController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClubService clubService;

    @Autowired
    private LeagueTableService leagueTableService;

    @GetMapping("/admin/league-table/add")
    public String addGet(Model model){
        model.addAttribute("form",new LeagueTableRequestForm());
        model.addAttribute("clubs",clubService.getAll());
        return ADD_LEAGUE_TABLE;
    }

    @PostMapping("/admin/league-table/add")
    public String addPost(@Valid @ModelAttribute("form") LeagueTableRequestForm form, BindingResult result,Model model){
        LOGGER.debug("form : {}",form);
        if(result.hasErrors()){
            model.addAttribute("clubs",clubService.getAll());
            return ADD_LEAGUE_TABLE;
        }else if(!clubService.existsById(form.getClubId())){
            result.addError(new FieldError("form","clubId","please choose a club"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_LEAGUE_TABLE;
        }else if(leagueTableService.existsByClubId(form.getClubId())){
            result.addError(new FieldError("form","clubId","club table already exists"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_LEAGUE_TABLE;

        }else {
            LeagueTable leagueTable = LeagueTable.builder()
                    .played(form.getPlayed())
                    .won(form.getWon())
                    .drawn(form.getDrawn())
                    .lost(form.getLost())
                    .points(form.getPoints())
                    .club(Club.builder().id(form.getClubId()).build())
                    .build();
            leagueTableService.add(leagueTable);
            return "redirect:/admin";
        }
    }
}