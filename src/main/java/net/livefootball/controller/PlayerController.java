package net.livefootball.controller;

import net.livefootball.form.PlayerRequestForm;
import net.livefootball.form.PlayerUpdateRequestForm;
import net.livefootball.model.Club;
import net.livefootball.model.Player;
import net.livefootball.service.ClubService;
import net.livefootball.service.PlayerService;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class PlayerController implements Pages{

    private static final int PAGE_SIZE = 6;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClubService clubService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PageableUtil pageableUtil;

    @GetMapping("/admin/player/add")
    public String addGet(Model model){
        model.addAttribute("clubs",clubService.getAll());
        model.addAttribute("form",new PlayerRequestForm());
        return ADD_PLAYER;
    }

    @PostMapping("/admin/player/add")
    public String addPost(@Valid @ModelAttribute("form")PlayerRequestForm form, BindingResult result,Model model){
        LOGGER.debug("form : {}",form);
        int age;
        if(result.hasErrors()){
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else if((age = generateAge(form.getBirthDate())) < 15 || age > 42){
            result.addError(new FieldError("form","birthDate","invalid birth date"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else if(form.getClubId() <= 0 || !clubService.existsById(form.getClubId())){
            result.addError(new FieldError("form","clubId","please choose a club"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else if(form.getImage().isEmpty()){
            result.addError(new FieldError("form","image","image is empty"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else if(!fileUtil.isValidImgFormat(form.getImage().getContentType())){
            result.addError(new FieldError("form","image","invalid image format"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else {
            Player player = Player.builder()
                    .name(form.getName())
                    .surname(form.getSurname())
                    .nationality(form.getNationality())
                    .birthDate(form.getBirthDate())
                    .age(age)
                    .position(form.getPosition())
                    .club(Club.builder().id(form.getClubId()).build())
                    .imgUrl("")
                    .build();
            playerService.add(player,form.getImage());
            return "redirect:/admin/players";
        }
    }

    @PostMapping("/admin/player/update")
    public String updatePost(@Valid @ModelAttribute("form")PlayerUpdateRequestForm form, BindingResult result,Model model){
        LOGGER.debug("form : {}",form);
        int age;
        MultipartFile image = null;
        if(result.hasErrors()){
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else if((age = generateAge(form.getBirthDate())) < 15 || age > 42){
            result.addError(new FieldError("form","birthDate","invalid birth date"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else if(form.getClubId() <= 0 || !clubService.existsById(form.getClubId())){
            result.addError(new FieldError("form","clubId","please choose a club"));
            model.addAttribute("clubs",clubService.getAll());
            return ADD_PLAYER;
        }else {
            if(form.getImage() != null && !form.getImage().isEmpty() && fileUtil.isValidImgFormat(form.getImage().getContentType())){
                image = form.getImage();
            }
            Player persistPlayer = playerService.getById(form.getId());
            Player player = Player.builder()
                    .id(form.getId())
                    .name(form.getName())
                    .surname(form.getSurname())
                    .nationality(form.getNationality())
                    .birthDate(form.getBirthDate())
                    .age(age)
                    .position(form.getPosition())
                    .club(Club.builder().id(form.getClubId()).build())
                    .imgUrl(persistPlayer.getImgUrl())
                    .build();
            playerService.update(player,image);
            return "redirect:/admin/players";
        }
    }

    private int generateAge(Date birthOfDate){
        String[] birthOfDateStr = DATE_FORMAT.format(birthOfDate).split("-");
        String[] currentDateStr = DATE_FORMAT.format(new Date()).split("-");
        int birthYear = Integer.parseInt(birthOfDateStr[0]);
        int birthMonth = birthOfDateStr[1].startsWith("0") ? Integer.parseInt(String.valueOf(birthOfDateStr[1].charAt(1))) : Integer.parseInt(birthOfDateStr[1]);
        int birthDay = birthOfDateStr[2].startsWith("0") ? Integer.parseInt(String.valueOf(birthOfDateStr[2].charAt(1))) : Integer.parseInt(birthOfDateStr[2]);
        int currentYear = Integer.parseInt(currentDateStr[0]);
        int currentMonth = currentDateStr[1].startsWith("0") ? Integer.parseInt(String.valueOf(currentDateStr[1].charAt(1))) : Integer.parseInt(currentDateStr[1]);
        int currentDay = currentDateStr[2].startsWith("0") ? Integer.parseInt(String.valueOf(currentDateStr[2].charAt(1))) : Integer.parseInt(currentDateStr[2]);
        int monthSubtraction = 0;
        if(currentMonth - birthMonth == 0){
            monthSubtraction = currentDay - birthDay < 0 ? 1 : 0;
        }else if(currentMonth - birthMonth < 0){
            monthSubtraction = 1;
        }
        return (currentYear - birthYear) - monthSubtraction;
    }

    @GetMapping("/club/{id}/players")
    public String clubPlayers(@PathVariable("id")String strId, Model model, Pageable pageable){
        int clubId;
        try {
            if(!playerService.existsById((clubId =Integer.parseInt(strId)))){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        int length = pageableUtil.getLength(playerService.countByClubId(clubId),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("players",playerService.getAllByClubId(clubId,pageable));
        model.addAttribute("pageNumber",pageable.getPageNumber());
        model.addAttribute("length",length);
        return PLAYERS;
    }

    @GetMapping("/admin/players")
    public String adminPlayers(Pageable pageable,Model model){
        int length = pageableUtil.getLength(playerService.count(),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("players",playerService.getAll(pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return "admin/" + PLAYERS;
    }

    @GetMapping("/admin/player/delete/{id}")
    public String delete(@PathVariable("id")String strId){
        try {
            playerService.deleteById(Integer.parseInt(strId));
        }catch (NumberFormatException e){}
        return "redirect:/admin/players";
    }

    @GetMapping("/admin/player/update/{id}")
    public String adminUpdate(@PathVariable("id")String strId,Model model){
        Player player;
        try {
            if((player = playerService.getById(Integer.parseInt(strId))) == null){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/admin/players";
        }
        PlayerUpdateRequestForm form = new PlayerUpdateRequestForm();
        form.setId(player.getId());
        form.setName(player.getName());
        form.setSurname(player.getSurname());
        form.setNationality(player.getNationality());
        form.setBirthDate(player.getBirthDate());
        form.setPosition(player.getPosition());
        form.setClub(player.getClub());
        model.addAttribute("form",form);
        model.addAttribute("clubs",clubService.getAll());
        return UPDATE_PLAYER;
    }
}