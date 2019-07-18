package com.game.project.kalahgame.repository;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for @{@link GameBoard}
 */
@Repository
public interface BoardRepository extends CrudRepository<GameBoard, Long> {
    /**
     * Find a board by instance of @{@link Game}
     *
     * @param game @{@link Game} instance
     * @return @{@link GameBoard} instance
     */
    public GameBoard findByGame(Game game);
}