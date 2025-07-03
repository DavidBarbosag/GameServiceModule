package com.eci.ARSW.GameService.GameService.repository;

import com.eci.ARSW.GameService.GameService.model.GameState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends MongoRepository<GameState, String> {
}
