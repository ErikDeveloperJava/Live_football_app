package net.livefootball.repository;

import net.livefootball.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    int countByNewsId(int newsId);

    List<Comment> findAllByNewsId(int newsId, Pageable pageable);
}