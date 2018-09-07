package net.livefootball.service.impl;

import net.livefootball.model.User;
import net.livefootball.repository.UserRepository;
import net.livefootball.service.UserService;
import net.livefootball.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUtil fileUtil;

    @Transactional(rollbackFor = Exception.class)
    public void add(User user, MultipartFile multipartFile) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String img = System.currentTimeMillis() + user.getUsername() + ".jpg";
        try {
            fileUtil.saveImage("users\\" + user.getId(),img,multipartFile);
            user.setImgUrl(user.getId() + "/" + img);
            LOGGER.debug("user saved");
        }catch (Exception e){
            fileUtil.delete("users\\" + user.getId(),true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> getAllByIdNotIn(Pageable pageable, int... array) {
        if(array.length == 0){
            return userRepository.findAll(pageable).getContent();
        }else {
            return userRepository.findAllByIdNotIn(array[0],pageable);
        }
    }

    @Override
    public int countByIdNotIn(int... array) {
        if(array.length == 0){
            return (int) userRepository.count();
        }else {
            return userRepository.countByIdNotIn(array[0]);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        fileUtil.delete("users\\" + id,true);
        userRepository.deleteById(id);
        LOGGER.debug("user deleted");
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }


}
