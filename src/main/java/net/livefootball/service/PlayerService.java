package net.livefootball.service;

import net.livefootball.model.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService {

    void add(Player player, MultipartFile image);

    List<Player> getAllByClubId(int clubId, Pageable pageable);

    int countByClubId(int clubId);

    boolean existsById(int id);

    List<Player> getAll(Pageable pageable);

    int count();

    void deleteById(int id);

    Player getById(int id);

    void update(Player player,MultipartFile image);
}