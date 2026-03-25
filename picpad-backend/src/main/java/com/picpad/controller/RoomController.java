package com.picpad.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.picpad.entity.Room;
import com.picpad.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoomController {

    public RoomService roomService;

    @PostMapping("/{roomCode}")
    public Room createRoom(@PathVariable String roomCode)
    {
        return roomService.createRoom(roomCode);
    }

}
