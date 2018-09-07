package net.livefootball.service.impl;

import net.livefootball.model.Player;
import net.livefootball.repository.PlayerRepository;
import net.livefootball.service.PlayerService;
import net.livefootball.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

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
            fileUtil.saveImage("players\\" + player.getId(),imgUrl,image);
            player.setImgUrl(player.getId() + "/" +  imgUrl);
            LOGGER.debug("player saved");
        }catch (Exception e){
            fileUtil.delete("players\\" + player.getId(),true);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Player> getAllByClubId(int clubId, Pageable pageable) {
        return playerRepository.findAllByClubId(clubId,pageable);
    }

    @Override
    public int countByClubId(int clubId) {
        return playerRepository.countByClubId(clubId);
    }

    @Override
    public boolean existsById(int id) {
        return playerRepository.existsById(id);
    }

    @Override
    public List<Player> getAll(Pageable pageable) {
        return playerRepository.findAll(pageable).getContent();
    }

    @Override
    public int count() {
        return (int) playerRepository.count();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(int id) {
        playerRepository.deleteById(id);
        fileUtil.delete("players\\" + id,true);
        LOGGER.debug("player deleted");
    }

    @Override
    public Player getById(int id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Player player, MultipartFile image) {
        playerRepository.save(player);
        if(image != null){
            fileUtil.saveImage("players",player.getImgUrl(),image);
        }
        LOGGER.debug("player updated");
    }
}
