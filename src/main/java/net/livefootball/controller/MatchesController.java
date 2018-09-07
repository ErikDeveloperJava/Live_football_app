package net.livefootball.controller;

import net.livefootball.form.MatchUpdateErrorResponseForm;
import net.livefootball.form.MatchUpdateRequestForm;
import net.livefootball.form.MatchesRequestForm;
import net.livefootball.model.Club;
import net.livefootball.model.Matches;
import net.livefootball.service.ClubService;
import net.livefootball.service.MatchesService;
import net.livefootball.util.PageableUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MatchesController implements Pages {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClubService clubService;

    @Autowired
    private MatchesService matchesService;

    @Autowired
    private PageableUtil pageableUtil;

    @GetMapping("/admin/match/add")
    public String addGet(Model model) {
        model.addAttribute("clubs", clubService.getAll());
        model.addAttribute("form", new MatchesRequestForm());
        return ADD_MATCHES;
    }

    @PostMapping("/admin/match/add")
    public String addPost(@Valid @ModelAttribute("form") MatchesRequestForm form, BindingResult result, Model model) {
        LOGGER.debug("form : {}", form);
        Date date;
        if (result.hasErrors()) {
            model.addAttribute("clubs", clubService.getAll());
            return ADD_MATCHES;
        } else if (!clubService.existsById(form.getMasterId())) {
            model.addAttribute("clubs", clubService.getAll());
            result.addError(new FieldError("form", "masterId", "please choose a club"));
            return ADD_MATCHES;
        } else if (!clubService.existsById(form.getGuestId())) {
            model.addAttribute("clubs", clubService.getAll());
            result.addError(new FieldError("form", "guestId", "please choose a club"));
            return ADD_MATCHES;
        } else if ((date = generateDateAndTime(form.getDate(), form.getTime())) == null) {
            result.addError(new FieldError("form", "time", "invalid time format"));
            model.addAttribute("clubs", clubService.getAll());
            return ADD_MATCHES;
        } else {
            Matches matches = Matches.builder()
                    .master(Club.builder().id(form.getMasterId()).build())
                    .guest(Club.builder().id(form.getGuestId()).build())
                    .date(date)
                    .account(form.getAccount().equals("") ? "?" : form.getAccount())
                    .build();
            matchesService.add(matches);
            return "redirect:/admin";
        }
    }

    private Date generateDateAndTime(Date date, String strTime) {
        DATE_FORMAT.applyPattern("yyyy-MM-dd");
        String strDate = DATE_FORMAT.format(date);
        DATE_FORMAT.applyPattern("yyyy-MM-dd HH:mm");
        try {
            return DATE_FORMAT.parse(strDate + " " + strTime);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/matches/soon")
    public String soonMatches(Pageable pageable, Model model) {
        int length = pageableUtil.getLength(matchesService.countByDateGreaterThan(new Date()), pageable.getPageSize());
        pageable = pageableUtil.getCheckedPageable(pageable, length);
        model.addAttribute("matches", matchesService.geAllByDateGreaterThan(pageable));
        model.addAttribute("matchesName", "Soon matches");
        model.addAttribute("pageNumber", pageable.getPageNumber());
        model.addAttribute("length", length);
        return MATCHES;
    }

    @GetMapping("/matches/completed")
    public String completedMatches(Pageable pageable, Model model) {
        int length = pageableUtil.getLength(matchesService.countByDateLessThan(new Date()), pageable.getPageSize());
        pageable = pageableUtil.getCheckedPageable(pageable, length);
        model.addAttribute("matches", matchesService.geAllByDateLessThan(pageable));
        model.addAttribute("matchesName", "Completed matches");
        model.addAttribute("pageNumber", pageable.getPageNumber());
        model.addAttribute("length", length);
        return MATCHES;
    }

    @GetMapping("/matches/todays")
    public String todayMatches(Pageable pageable, Model model) {
        int length = pageableUtil.getLength(matchesService.countByDateBetween(), pageable.getPageSize());
        pageable = pageableUtil.getCheckedPageable(pageable, length);
        model.addAttribute("matches", matchesService.getAllByDateBetween(pageable));
        model.addAttribute("matchesName", "Today's matches");
        model.addAttribute("pageNumber", pageable.getPageNumber());
        model.addAttribute("length", length);
        return MATCHES;
    }

    @GetMapping({"/admin/matches", "/admin/matches/todays", "/admin/matches/completed",
            "/admin/matches/soon"})
    public String adminMatches(Pageable pageable, Model model, HttpServletRequest request) {
        int length = 0;
        switch (request.getRequestURI()) {
            case "/admin/matches":
                length = pageableUtil.getLength(matchesService.count(), pageable.getPageSize());
                pageable = pageableUtil.getCheckedPageable(pageable, length);
                model.addAttribute("matchesList", matchesService.getAll(pageable));
                model.addAttribute("length", length);
                model.addAttribute("pageNumber", pageable.getPageNumber());
                break;
            case "/admin/matches/todays":
                length = pageableUtil.getLength(matchesService.countByDateBetween(), pageable.getPageSize());
                pageable = pageableUtil.getCheckedPageable(pageable, length);
                model.addAttribute("matchesList", matchesService.getAllByDateBetween(pageable));
                model.addAttribute("length", length);
                model.addAttribute("pageNumber", pageable.getPageNumber());
                break;
            case "/admin/matches/completed":
                length = pageableUtil.getLength(matchesService.countByDateLessThan(new Date()), pageable.getPageSize());
                pageable = pageableUtil.getCheckedPageable(pageable, length);
                model.addAttribute("matchesList", matchesService.geAllByDateLessThan(pageable));
                model.addAttribute("length", length);
                model.addAttribute("pageNumber", pageable.getPageNumber());
                break;
            case "/admin/matches/soon":
                length = pageableUtil.getLength(matchesService.countByDateGreaterThan(new Date()), pageable.getPageSize());
                pageable = pageableUtil.getCheckedPageable(pageable, length);
                model.addAttribute("matchesList", matchesService.geAllByDateGreaterThan(pageable));
                model.addAttribute("length", length);
                model.addAttribute("pageNumber", pageable.getPageNumber());
        }
        return ADMIN_MATCHES;
    }

    @PostMapping("/admin/match/update")
    public @ResponseBody
    ResponseEntity update(@RequestBody MatchUpdateRequestForm form) {
        Club master;
        Club guest;
        int errorCount = 0;
        Date date;
        MatchUpdateErrorResponseForm errorResponseForm = new MatchUpdateErrorResponseForm();
        if ((master = clubService.getByName(form.getMaster())) == null) {
            errorResponseForm.setMasterError(true);
            errorCount++;
        }
        if((guest = clubService.getByName(form.getGuest())) == null){
            errorResponseForm.setGuestError(true);
            errorCount++;
        }
        if((date = isValidDate(form.getDate())) == null){
            errorResponseForm.setDateError(true);
            errorCount++;
        }
        if(!isValidTime(form.getTime())){
            errorResponseForm.setTimeError(true);
            errorCount++;
        }
        if(form.isCompleted() && !isValidAccount(form.getAccount())){
            errorResponseForm.setAccountError(true);
            errorCount++;
        }
        if(errorCount != 0){
            return ResponseEntity
                    .status(400)
                    .body(errorResponseForm);
        }else{
            Matches matches = matchesService.getById(form.getId());
            matches.setMaster(master);
            matches.setGuest(guest);
            matches.setDate(generateDateAndTime(date,form.getTime()));
            matches.setAccount(form.isCompleted() ? form.getAccount() : "?");
            matchesService.add(matches);
            form.setDate(new SimpleDateFormat("dd.MM.yyyy").format(date));
            return ResponseEntity
                    .ok(form);
        }
    }

    private Date isValidDate(String date){
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(date);
        }catch (Exception e){
            return null;
        }
    }

    private boolean isValidTime(String time){
        try {
            Integer.parseInt(time.substring(0,2));
            if(time.charAt(2) != ':'){
                throw new NumberFormatException();
            }
            Integer.parseInt(time.substring(3));
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    private boolean isValidAccount(String account){
        try {
            try {
                Integer.parseInt(account.substring(0,2));
            }catch (NumberFormatException e){
                Integer.parseInt(account.substring(0,1));
            }
            if(account.charAt(2) != '-'){
                if(account.charAt(1) != '-') {
                    throw new NumberFormatException();
                }
            }
            try {
                Integer.parseInt(account.substring(3));
            }catch (NumberFormatException e){
                Integer.parseInt(account.substring(2));
            }

        }catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    @GetMapping("/admin/matches/delete/{id}")
    public String delete(@PathVariable("id")String strId){
        try {
            matchesService.deleteById(Integer.parseInt(strId));
        }catch (NumberFormatException e){}
        return "redirect:/admin/matches";
    }
}