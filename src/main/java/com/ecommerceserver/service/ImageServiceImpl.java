package com.ecommerceserver.service;

import com.ecommerceserver.repository.ImageRepository;
import com.ecommerceserver.service.dto.ImageDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service("imageService")
public class ImageServiceImpl implements ImageService {

    @Resource(name = "imageRepository")
    private ImageRepository imageRepository;


    @Override
    public ImageDto saveImage(MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public boolean isImage(MultipartFile file) {
        return false;
    }

    @Override
    public org.springframework.core.io.Resource getImageAsResource(String fileName) {
        return null;
    }

    @Override
    public org.springframework.core.io.Resource getFullImageAsResource(String fileName) {
        return null;
    }

    @Override
    public List<ImageDto> getImages() {

        return null;
    }
}
