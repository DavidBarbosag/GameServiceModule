package com.eci.ARSW.GameService.GameService.repository;

import com.eci.ARSW.GameService.GameService.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * PlayerRepository is a Spring Data MongoDB repository interface, responsible for
 * persisting and retrieving Player objects from the MongoDB database.
 */
@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
}