package com.eci.ARSW.GameService.GameService.service;

import com.eci.ARSW.GameService.GameService.model.Mine;
import com.eci.ARSW.GameService.GameService.repository.MineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MineService {

    @Autowired
    private MineRepository mineRepository;

    public Mine saveMine(Mine mine) {
        return mineRepository.save(mine);
    }

    public Optional<Mine> getMineById(String id) {
        return mineRepository.findById(id);
    }

    public List<Mine> getAllMines() {
        return mineRepository.findAll();
    }

    public void deleteMine(String id) {
        mineRepository.deleteById(id);
    }
}