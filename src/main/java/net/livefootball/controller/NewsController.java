package net.livefootball.controller;

import net.livefootball.form.NewsRequestForm;
import net.livefootball.form.NewsUpdateRequestForm;
import net.livefootball.model.League;
import net.livefootball.model.News;
import net.livefootball.service.LeagueService;
import net.livefootball.service.NewsService;
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
import java.util.Date;
import java.util.List;

@Controller
public class NewsController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int PAGE_SIZE = 6;

    @Autowired
    private NewsService newsService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private PageableUtil pageableUtil;

    @GetMapping("/admin/news/add")
    public String addGet(Model model){
        model.addAttribute("form",new NewsRequestForm());
        model.addAttribute("leagues",leagueService.getAll());
        return ADD_NEWS;
    }

    @PostMapping("/admin/news/add")
    public String addPost(@Valid @ModelAttribute("form") NewsRequestForm form, BindingResult result,Model model){
        LOGGER.debug("form : {}",form);
        if(result.hasErrors()){
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_NEWS;
        }else if(form.getLeagueId() <= 0 || !leagueService.existsById(form.getLeagueId())){
            result.addError(new FieldError("form","leagueId","please choose a league"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_NEWS;
        }else if(form.getImage().isEmpty()){
            result.addError(new FieldError("form","image","image is empty"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_NEWS;
        }else if(!fileUtil.isValidImgFormat(form.getImage().getContentType())){
            result.addError(new FieldError("form","image","invalid image format"));
            model.addAttribute("leagues",leagueService.getAll());
            return ADD_NEWS;
        }else {
            News news = News.builder()
                    .title(form.getTitle())
                    .description(form.getDescription())
                    .createdDate(new Date())
                    .imgUrl("")
                    .videoUrl("?")
                    .league(League.builder().id(form.getLeagueId()).build())
                    .build();
            newsService.add(news,form.getImage(),form.getVideo());
            return "redirect:/admin/news";
        }
    }

    @GetMapping("/news")
    public String news(Pageable pageable,Model model){
        int length = pageableUtil.getLength(newsService.count(),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("newsPageList",newsService.getNewsPage(pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return NEWS;
    }

    @GetMapping("/news/{id}")
    public String newsDetails(@PathVariable("id")String strId,Model model){
        LOGGER.debug("id : {}",strId);
        int id;
        try {
            if(!newsService.existsById((id = Integer.parseInt(strId)))){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/";
        }
        model.addAttribute("newsDetails",newsService.getDetailsById(id));
        return NEWS_DETAILS;
    }

    @GetMapping("/admin/news")
    public String adminNews(Pageable pageable,Model model){
        int length = pageableUtil.getLength(newsService.count(),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("newsList",newsService.getAll(pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return "admin/" + NEWS;
    }

    @GetMapping("/admin/news/delete/{id}")
    public String delete(@PathVariable("id")String strId){
        try {
            newsService.deleteById(Integer.parseInt(strId));
        }catch (NumberFormatException e){}
        return "redirect:/admin/news";
    }

    @GetMapping("/admin/news/update/{id}")
    public String updateGet(@PathVariable("id")String strId,Model model){
        News news;
        try {
            if((news = newsService.getById(Integer.parseInt(strId))) == null){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException e){
            return "redirect:/admin/news";
        }
        NewsUpdateRequestForm form = new NewsUpdateRequestForm();
        form.setId(news.getId());
        form.setTitle(news.getTitle());
        form.setDescription(news.getDescription());
        form.setLeague(news.getLeague());
        model.addAttribute("form",form);
        model.addAttribute("leagues",leagueService.getAll());
        return UPDATE_NEWS;
    }

    @PostMapping("/admin/news/update")
    public String updatePost(@Valid @ModelAttribute("form")NewsUpdateRequestForm form,BindingResult result,
                             Model model){
        MultipartFile image = null;
        MultipartFile video = null;
        if(result.hasErrors()){
            model.addAttribute("leagues",leagueService.getAll());
            return UPDATE_NEWS;
        }else if(!leagueService.existsById(form.getLeagueId())){
            result.addError(new FieldError("form","leagueId","please choose a league"));
            model.addAttribute("leagues",leagueService.getAll());
            return UPDATE_NEWS;
        }
        if(form.getImage() != null && !form.getImage().isEmpty() && fileUtil.isValidImgFormat(form.getImage().getContentType())){
            image = form.getImage();
        }
        if(form.getVideo() != null && !form.getVideo().isEmpty() && fileUtil.isValidVideoFormat(form.getVideo().getContentType())){
            video = form.getVideo();
        }
        News persistNews = newsService.getById(form.getId());
        String videoUrl;
        if(!persistNews.getVideoUrl().equals("?") && video != null){
            videoUrl = persistNews.getId() + "/" + System.currentTimeMillis() + video.getOriginalFilename();
        }else if(persistNews.getVideoUrl().equals("?") && video!= null){
            videoUrl = persistNews.getId() + "/" + System.currentTimeMillis() + video.getOriginalFilename();
        }else {
            videoUrl = "?";
        }
        News news = News.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .createdDate(persistNews.getCreatedDate())
                .league(persistNews.getLeague())
                .imgUrl(persistNews.getImgUrl())
                .videoUrl(videoUrl)
                .build();
        newsService.update(news,image,video);
        return "redirect:/admin/news";
    }
}