package com.game.project.kalahgame.repository;

import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for @{@link Pit}
 */
@Repository
public interface PitRepository extends CrudRepository<Pit, Long> {
    /**
     * Find Pit by @{@link GameBoard}
     *
     * @param GameBoard @{@link GameBoard} to find pits by
     * @return List of @{@link Pit}
     */
    List<Pit> findByGameBoardOrderByPositionAsc(GameBoard GameBoard);

    /**
     * Find Pit by @{@link GameBoard} and position
     *
     * @param board @{@link GameBoard} to find by
     * @param position to find by
     * @return @{@link Pit} by board and position
     */
    Pit findByGameBoardAndPosition(GameBoard board, int position);
}