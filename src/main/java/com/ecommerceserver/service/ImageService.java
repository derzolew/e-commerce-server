package com.ecommerceserver.service;

import com.ecommerceserver.service.dto.ImageDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    ImageDto saveImage(MultipartFile file) throws IOException;

    boolean isImage(MultipartFile file);

    Resource getImageAsResource(String fileName);

    Resource getFullImageAsResource(String fileName);

    List<ImageDto> getImages();
}
