package com.picpad.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.picpad.entity.Image;
import com.picpad.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class ImageService {

    public ImageRepository imageRepository;

    public Image saveImage(String roomCode, String imageUrl)
    {
        Image image = Image.builder()
                .roomCode(roomCode)
                .imageUrl(imageUrl)
                .createdAt(System.currentTimeMillis())
                .build();

        return imageRepository.save(image);
    }

    public List<Image> getImages(String roomCode)
    {
        return imageRepository.findByRoomCode(roomCode);
    }

    public void deleteImage(String id)
    {
        imageRepository.deleteById(id);
    }

}
