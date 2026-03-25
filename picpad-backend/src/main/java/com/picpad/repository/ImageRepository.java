package com.picpad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.picpad.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

    List<Image> findByRoomCode(String roomCode);

}
