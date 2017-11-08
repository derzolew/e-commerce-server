package com.ecommerceserver.controllers;

import com.ecommerceserver.service.ImageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource(name = "imageService")
    private ImageServiceImpl imageService;

    @GetMapping(value = "/get")
    public ResponseEntity getImage()
    {

        return ResponseEntity.ok(null);
    }
}
