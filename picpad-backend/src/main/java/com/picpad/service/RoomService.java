package com.picpad.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.picpad.entity.Room;
import com.picpad.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room createRoom(String roomCode)
    {
        if(roomRepository.existsById(roomCode))
            return roomRepository.findById(roomCode).get();

        Room room = Room.builder()
                .roomCode(roomCode)
                .createdAt(System.currentTimeMillis())
                .build();

        return roomRepository.save(room);
    }

}
