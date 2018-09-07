package net.livefootball.util;

import com.sun.media.jfxmedia.control.VideoFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUtil {

    private static final String[] VIDEO_FORMATS = {"video/mp4"};
    private static final String[] IMAGE_FORMATS = {"image/jpeg", "image/png"};
    private static final String[] DEFAULT_IMAGE_DIRS = {"clubs", "gallery", "leagues", "news", "users","players"};

    @Value("${images.path}")
    private String imagesPath;

    @Value("${video.path}")
    private String videosPath;

    @PostConstruct
    public void init() {
        File imageFile = new File(imagesPath);
        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        for (String defaultImageDir : DEFAULT_IMAGE_DIRS) {
            createImagesDir(defaultImageDir);
        }
        File file = new File(videosPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void createImagesDir(String dir) {
        File file = new File(imagesPath, dir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public boolean isValidImgFormat(String format){
        for (String imageFormat : IMAGE_FORMATS) {
            if(imageFormat.equals(format)){
                return true;
            }
        }
        return false;
    }

    public boolean isValidVideoFormat(String format){
        for (String videoFormat : VIDEO_FORMATS) {
            return videoFormat.equals(format);
        }
        return false;
    }

    public void saveImage(String dir, String img, MultipartFile multipartFile) {
        File file = new File(imagesPath, dir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            multipartFile.transferTo(new File(file, img));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveVideo(String dir, String video, MultipartFile multipartFile) {
        File file = new File(videosPath, dir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            multipartFile.transferTo(new File(file, video));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String fileName, boolean isImg) {
        File file;
        if (isImg) {
            file = new File(imagesPath,fileName);
        }else {
            file = new File(videosPath,fileName);
        }
        if(file.exists()){
            delete(file);
        }
    }

    private void delete(File file){
        if(file.isDirectory()){
            for (File f : file.listFiles()) {
                delete(f);
            }
            file.delete();
        }else {
            file.delete();
        }
    }
}