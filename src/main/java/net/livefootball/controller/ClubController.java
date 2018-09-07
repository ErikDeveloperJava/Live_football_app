package net.livefootball.controller;

import net.livefootball.form.ClubRequestForm;
import net.livefootball.form.ClubUpdateRequestForm;
import net.livefootball.model.Club;
import net.livefootball.model.League;
import net.livefootball.service.ClubService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class ClubController implements Pages {

    private static final int PAGE_SIZE = 6;
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private PageableUtil pageableUtil;

    @GetMapping("/admin/club/add")
    public String addGet(Model model) {
        model.addAttribute("leagues",leagueService.getAll());
        model.addAttribute("form", new ClubRequestForm());
        return ADD_CLUB;
    }

    @PostMapping("/admin/club/add")
    public String addPost(@Valid @ModelAttribute("form") ClubRequestForm form, BindingResult result,Model model) {
        LOGGER.debug("form : {}", form);
        if (result.hasErrors()) {
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_CLUB;
        } else if (!form.getChampion().equals("yes") && !form.getChampion().equals("no")) {
            result.addError(new FieldError("form","champion","please choose Yes or No"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_CLUB;
        }else if(form.getChampion().equals("yes") && clubService.getByIsChampion(form.getLeagueId()) != null){
            result.addError(new FieldError("form","champion","champion already exists"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_CLUB;
        }else if(form.getLeagueId() <= 0 || !leagueService.existsById(form.getLeagueId())){
            result.addError(new FieldError("form","leagueId","please choose a league"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_CLUB;
        }else if(form.getImage().isEmpty()){
            result.addError(new FieldError("form","image","image is empty"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_CLUB;
        }else if(!fileUtil.isValidImgFormat(form.getImage().getContentType())){
            result.addError(new FieldError("form","image","invalid image format"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_CLUB;
        }else {
            Club club = Club.builder()
                    .name(form.getName())
                    .description(form.getDescription())
                    .trainer(form.getTrainer())
                    .stadium(form.getStadium())
                    .owner(form.getOwner())
                    .champion(form.getChampion().endsWith("yes"))
                    .league(League.builder().id(form.getLeagueId()).build())
                    .imgUrl("")
                    .build();
            clubService.add(club,form.getImage());
            return "redirect:/admin/clubs";
        }
    }

    @PostMapping("/admin/club/update")
    public String update(@Valid @ModelAttribute("form") ClubUpdateRequestForm form, BindingResult result,Model model) {
        MultipartFile multipartFile = null;
        LOGGER.debug("form : {}", form);
        if (result.hasErrors()) {
            model.addAttribute("leagues", leagueService.getAll());
            return UPDATE_CLUB;
        } else if (!form.getChampion().equals("true") && !form.getChampion().equals("false")) {
            result.addError(new FieldError("form", "champion", "please choose Yes or No"));
            model.addAttribute("leagues", leagueService.getAll());
            return UPDATE_CLUB;
        } else if (form.getChampion().equals("true") && clubService.getByIsChampion(form.getLeague().getId()) != null) {
            result.addError(new FieldError("form", "champion", "champion already exists"));
            model.addAttribute("leagues", leagueService.getAll());
            return UPDATE_CLUB;
        } else if (form.getLeague().getId() <= 0 || !leagueService.existsById(form.getLeague().getId())) {
            result.addError(new FieldError("form", "league.id", "please choose a league"));
            model.addAttribute("leagues", leagueService.getAll());
            return UPDATE_CLUB;
        } else {
            Club persistClub = clubService.getById(form.getId());
            if (form.getImage() != null && !form.getImage().isEmpty() && fileUtil.isValidImgFormat(form.getImage().getContentType())) {
                multipartFile = form.getImage();
            }
            Club club = Club.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .description(form.getDescription())
                    .trainer(form.getTrainer())
                    .stadium(form.getStadium())
                    .owner(form.getOwner())
                    .champion(new Boolean(form.getChampion()))
                    .league(form.getLeague())
                    .imgUrl(persistClub.getImgUrl())
                    .build();
            clubService.update(club, multipartFile);
            return "redirect:/admin/clubs";
        }
    }

    @GetMapping("/clubs")
    public String clubs(Pageable pageable,Model model){
        int length = pageableUtil.getLength(clubService.count(),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("clubs",clubService.getAll(pageable));
        model.addAttribute("pageNumber",pageable.getPageNumber());
        model.addAttribute("length",length);
        return CLUBS;
    }

    @GetMapping("/league/{id}/clubs")
    public String leagueClubs(@PathVariable("id")String strId, Pageable pageable, Model model){
        try {
            if(!leagueService.existsById(Integer.parseInt(strId))){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        int length = pageableUtil.getLength(clubService.countByLeagueId(Integer.parseInt(strId)),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("clubs",clubService.getAllByLeagueId(Integer.parseInt(strId),pageable));
        model.addAttribute("pageNumber",pageable.getPageNumber());
        model.addAttribute("length",length);
        return CLUBS;
    }

    @GetMapping("/club/{id}")
    public String clubDetails(@PathVariable("id")String strId,Model model){
        try {
            if(!clubService.existsById(Integer.parseInt(strId))){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        model.addAttribute("clubPage",clubService.getClubPageById(Integer.parseInt(strId)));
        return CLUB_DETAILS;
    }

    @GetMapping("/admin/clubs")
    public String adminClubs(Pageable pageable,Model model){
        int length = pageableUtil.getLength(clubService.count(),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("clubs",clubService.getAll(pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return "admin/"+ CLUBS;
    }

    @GetMapping("/admin/club/delete/{id}")
    public String delete(@PathVariable("id") String strId){
        LOGGER.debug("clubId : {}",strId);
        try {
            clubService.deleteById(Integer.parseInt(strId));
        }catch (NumberFormatException e){
            //****
        }
        return "redirect:/admin/clubs";
    }

    @GetMapping("/admin/club/update/{id}")
    public String update(@PathVariable("id") String strId,Model model){
        try {
            if(!clubService.existsById(Integer.parseInt(strId))){
                throw new NumberFormatException();
            }
            Club club = clubService.getById(Integer.parseInt(strId));
            ClubUpdateRequestForm form = new ClubUpdateRequestForm();
            form.setId(club.getId());
            form.setName(club.getName());
            form.setTrainer(club.getTrainer());
            form.setStadium(club.getStadium());
            form.setOwner(club.getOwner());
            form.setLeague(club.getLeague());
            form.setChampion(club.isChampion() ? "true" : "false");
            form.setDescription(club.getDescription());
            model.addAttribute("leagues",leagueService.getAll());
            model.addAttribute("form",form);
            return UPDATE_CLUB;
        }catch (NumberFormatException e){
            return "redirect:/admin/clubs";
        }
    }
}