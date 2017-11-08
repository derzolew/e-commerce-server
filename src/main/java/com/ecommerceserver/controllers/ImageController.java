package com.ecommerceserver.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    @GetMapping(value = "/get")
    public ResponseEntity<Resource> getImage()
    {
        return ResponseEntity.ok(null);
    }
}
