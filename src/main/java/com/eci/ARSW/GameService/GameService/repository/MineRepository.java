package com.eci.ARSW.GameService.GameService.repository;

import com.eci.ARSW.GameService.GameService.model.Mine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MineRepository extends MongoRepository<Mine, String> {
}