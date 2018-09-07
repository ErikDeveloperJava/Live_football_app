package net.livefootball.service.impl;

import net.livefootball.dto.CommentDto;
import net.livefootball.dto.UserDto;
import net.livefootball.model.Comment;
import net.livefootball.repository.CommentRepository;
import net.livefootball.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<CommentDto> getCommentDtoByNewsId(int newsId,Pageable pageable) {
        List<Comment> commentList = commentRepository.findAllByNewsId(newsId, pageable);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(CommentDto.builder()
                    .comment(comment.getComment())
                    .sendDate(DATE_FORMAT.format(comment.getSendDate()))
                    .user(UserDto.builder()
                            .name(comment.getUser().getName())
                            .imgUrl(comment.getUser().getImgUrl())
                            .build())
                    .build());
        }
        return commentDtoList;
    }

    @Transactional
    public void add(Comment comment) {
        commentRepository.save(comment);
        LOGGER.debug("comment saved");
    }
}
