package com.ecommerceserver.controllers;

import com.ecommerceserver.service.ImageServiceImpl;
import com.ecommerceserver.service.dto.ImageDto;
import com.ecommerceserver.service.exception.BadImageSizeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource(name = "imageService")
    private ImageServiceImpl imageService;

    @GetMapping(value = "/{imageName:.+}", produces = "image/jpeg")
    public ResponseEntity<org.springframework.core.io.Resource> getImage(@PathVariable String imageName) {
        org.springframework.core.io.Resource file = this.imageService.getFullImageAsResource(imageName);
        return ResponseEntity.ok(file);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ImageDto> uploadImage(@RequestParam("image") MultipartFile image) throws IOException, BadImageSizeException, InterruptedException {
        if (image == null || !imageService.isImage(image)) {
            return ResponseEntity.badRequest().build();
        }
        ImageDto result = imageService.saveImage(image);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "all")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> imageDtoList = imageService.getImages();
        return ResponseEntity.ok(imageDtoList);
    }

    @PutMapping(value = "/delete/{imageName:.+}")
    public ResponseEntity deleteImage(@PathVariable String imageName) {
        try {
            imageService.deleteImage(imageName);
            return ResponseEntity.ok(imageName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
