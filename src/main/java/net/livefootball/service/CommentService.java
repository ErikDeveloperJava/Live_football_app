package net.livefootball.service;

import net.livefootball.dto.CommentDto;
import net.livefootball.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    List<CommentDto> getCommentDtoByNewsId(int newsId, Pageable pageable);

    void add(Comment comment);
}