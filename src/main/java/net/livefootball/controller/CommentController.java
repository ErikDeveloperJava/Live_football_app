package net.livefootball.controller;

import net.livefootball.model.Comment;
import net.livefootball.model.User;
import net.livefootball.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/news/comment")
public class CommentController {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int PAGE_SIZE = 4;

    @Autowired
    private CommentService commentService;

    @GetMapping("/{newsId}/page/{pageNumber}")
    public ResponseEntity getCommentList(@PathVariable("newsId") int newsId,
                                         @PathVariable("pageNumber")int pageNumber) {
        LOGGER.debug("newsId : {},pageNumber : {}",newsId,pageNumber);
        return ResponseEntity
                .ok(commentService.getCommentDtoByNewsId(newsId,
                        PageRequest.of(pageNumber, PAGE_SIZE,Sort.Direction.DESC,"sendDate")));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Comment comment,
                              @RequestAttribute("user")User user){
        if(comment.getComment()== null || comment.getComment().length() < 1){
            return ResponseEntity
                    .badRequest()
                    .body("invalid comment");
        }
        comment.setSendDate(new Date());
        comment.setUser(user);
        commentService.add(comment);
        return ResponseEntity
                .ok()
                .body("success");
    }
}