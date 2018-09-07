package net.livefootball.service;

import net.livefootball.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void add(User user, MultipartFile multipartFile);

    boolean existsByUsername(String username);

    List<User> getAllByIdNotIn(Pageable pageable,int...array);

    int countByIdNotIn(int...array);

    void deleteById(int id);

    List<User> getAll();
}