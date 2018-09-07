package net.livefootball.service.impl;

import net.livefootball.model.Gallery;
import net.livefootball.repository.GalleryRepository;
import net.livefootball.service.GalleryService;
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
public class GalleryServiceImpl implements GalleryService {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private FileUtil fileUtil;

    @Override
    public int count() {
        return (int) galleryRepository.count();
    }

    @Override
    public List<Gallery> getAll(Pageable pageable) {
        return galleryRepository.findAll(pageable).getContent();
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(Gallery gallery, MultipartFile image) {
        galleryRepository.save(gallery);
        try {
            fileUtil.saveImage("gallery",gallery.getUrl(),image);
            LOGGER.debug("gallery saved");
        }catch (Exception e){
            fileUtil.delete("gallery\\" + gallery.getUrl(),true);
            throw new RuntimeException(e);
        }
    }
}
