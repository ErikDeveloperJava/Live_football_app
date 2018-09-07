package net.livefootball.controller;

import net.livefootball.model.Gallery;
import net.livefootball.service.GalleryService;
import net.livefootball.util.FileUtil;
import net.livefootball.util.PageableUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GalleryController implements Pages{

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int ADMIN_PAGE_SIZE = 24;
    private static final int PAGE_SIZE = 8;

    @Autowired
    private GalleryService galleryService;

    @Autowired
    private PageableUtil pageableUtil;

    @Autowired
    private FileUtil fileUtil;

    @GetMapping("/admin/gallery")
    public String adminGallery(Model model, Pageable pageable){
        int count = galleryService.count();
        int length = pageableUtil.getLength(count,ADMIN_PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),ADMIN_PAGE_SIZE),length);
        model.addAttribute("galleryList",galleryService.getAll(pageable));
        model.addAttribute("pageNumber",pageable.getPageNumber());
        model.addAttribute("length",length);
        model.addAttribute("count",count);
        return "admin/" + GALLERY;
    }

    @PostMapping("/admin/gallery")
    public @ResponseBody
    ResponseEntity upload(MultipartFile image){
        if(image.isEmpty() || !fileUtil.isValidImgFormat(image.getContentType())){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        Gallery gallery = Gallery.builder()
                .url(System.currentTimeMillis() + image.getOriginalFilename().trim())
                .build();
        galleryService.add(gallery,image);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/gallery")
    public String gallery(Pageable pageable,Model model){
        int length = pageableUtil.getLength(galleryService.count(),PAGE_SIZE);
        pageable = pageableUtil.getCheckedPageable(PageRequest.of(pageable.getPageNumber(),PAGE_SIZE),length);
        model.addAttribute("galleryList",galleryService.getAll(pageable));
        model.addAttribute("length",length);
        model.addAttribute("pageNumber",pageable.getPageNumber());
        return GALLERY;
    }
}