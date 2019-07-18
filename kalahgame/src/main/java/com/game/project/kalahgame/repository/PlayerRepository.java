package com.game.project.kalahgame.repository;

import com.game.project.kalahgame.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for @{@link Player}
 */
@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    /**
     * Find Player by username
     *
     * @param username of the player
     * @return @{@link Player} by name
     */
    Player findOneByUsername(String username);
}
