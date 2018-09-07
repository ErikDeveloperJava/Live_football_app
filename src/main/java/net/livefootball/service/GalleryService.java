package net.livefootball.service;

import net.livefootball.model.Gallery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GalleryService {

    int count();

    List<Gallery> getAll(Pageable pageable);

    void add(Gallery gallery, MultipartFile image);
}