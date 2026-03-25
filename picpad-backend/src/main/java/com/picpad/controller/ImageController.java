package com.picpad.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.picpad.entity.Image;
import com.picpad.service.ImageService;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ImageController {

	@Autowired
    private final ImageService imageService;

    @PostMapping
    public Image saveImage(
            @RequestParam String roomCode,
            @RequestParam String imageUrl)
    {
        return imageService.saveImage(roomCode, imageUrl);
    }

    @GetMapping("/{roomCode}")
    public List<Image> getImages(@PathVariable String roomCode)
    {
        return imageService.getImages(roomCode);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable String id)
    {
        imageService.deleteImage(id);
    }

}
