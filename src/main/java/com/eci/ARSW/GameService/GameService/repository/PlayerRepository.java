package com.eci.ARSW.GameService.GameService.repository;

import com.eci.ARSW.GameService.GameService.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
}