package com.game.project.kalahgame.repository;


import com.game.project.kalahgame.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for @{@link Game}
 */
@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    /**
     * Find game by @{@link Game.State}
     *
     * @param gameState @{@link Game.State} to find by
     * @return List of @{@link Game} with given Game.State
     */
    List<Game> findByState(Game.State gameState);

}
