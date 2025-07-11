package com.eci.ARSW.GameService.GameService.repository;

import com.eci.ARSW.GameService.GameService.model.GameState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * GameStateRepository is a Spring Data MongoDB repository interface, responsible for
 * persisting and retrieving GameState objects from the MongoDB database.
 */
@Repository
public interface GameStateRepository extends MongoRepository<GameState, String> {
}
