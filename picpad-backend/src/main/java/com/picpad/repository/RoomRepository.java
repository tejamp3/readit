package com.picpad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.picpad.entity.Room;

public interface RoomRepository extends JpaRepository<Room, String> {
}
