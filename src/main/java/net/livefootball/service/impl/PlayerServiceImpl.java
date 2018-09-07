package net.livefootball.service.impl;

import net.livefootball.model.Player;
import net.livefootball.repository.PlayerRepository;
import net.livefootball.service.PlayerService;
import net.livefootball.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class PlayerRepositoryImpl implements PlayerService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private FileUtil fileUtil;

    @Transactional(rollbackFor = Exception.class)
    public void add(Player player, MultipartFile image) {
        playerRepository.save(player);
        String imgUrl = System.currentTimeMillis() + image.getOriginalFilename();
        try {
            fileUtil.saveImage("");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
