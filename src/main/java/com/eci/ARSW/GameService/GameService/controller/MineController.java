package com.eci.ARSW.GameService.GameService.controller;

import com.eci.ARSW.GameService.GameService.model.Mine;
import com.eci.ARSW.GameService.GameService.service.MineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mines")
public class MineController {

    @Autowired
    private MineService mineService;

    @PostMapping
    public Mine createMine(@RequestBody Mine mine) {
        return mineService.saveMine(mine);
    }

    @GetMapping("/{id}")
    public Optional<Mine> getMine(@PathVariable String id) {
        return mineService.getMineById(id);
    }

    @GetMapping
    public List<Mine> getAllMines() {
        return mineService.getAllMines();
    }

    @DeleteMapping("/{id}")
    public void deleteMine(@PathVariable String id) {
        mineService.deleteMine(id);
    }
}