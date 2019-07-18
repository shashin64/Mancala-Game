package com.game.project.kalahgame.service;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for @{@link Game} related actions
 */
@Service
@Transactional
public class GameService {

    private GameRepository gameRepository;

    /**
     * GameService constructor
     *
     * @param gameRepository @{@link GameRepository} dependency
     */
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Function to create a new game
     *
     * @param player @{@link Player} that created the game
     * @return @{@link Game} newly created Game
     */
    public Game createNewGame(Player player) {
        // Create game and set variables
        Game game  = new Game(player, player, Game.State.WAIT_FOR_OPPONENT);

        // Save game
        gameRepository.save(game);

        return game;
    }

    /**
     * Function to join a Game
     *
     * @param player @{@link Player} that wants to join the game
     * @param gameId The id of the Game to join
     * @return @{@link Game} that was joined
     */
    public Game joinGame(Player player, Long gameId) {
        // Find game by id
        Game game = getGameById(gameId);

        // Set Second player
        game.setSecondPlayer(player);

        // Update gamestate
        updateGameState(game, Game.State.GAME_IN_PLAY);

        // Save game
        gameRepository.save(game);

        return game;
    }

    /**
     * Function to change the player's turn
     *
     * @param player @{@link Player} to switch turn to
     * @param gameId The id of the Game
     * @return @{@link Game} that the turn changed for
     */
    public Game switchTurn(Player player, Long gameId) {
        // Find game by id
        Game game = getGameById(gameId);

        // Set player turn
        game.setPlayerTurn(player);

        // save game
        gameRepository.save(game);

        return game;
    }

    /**
     * Function to update the game state
     *
     * @param game @{@link Game} to update for
     * @param gameState @{@link Game.State} to change to
     * @return @{@link Game} that was changed
     */
    public Game updateGameState(Game game, Game.State gameState) {
        // Find game by id
        Game game1 = getGameById(game.getId());

        // Change state
        game1.setState(gameState);

        // save game
        gameRepository.save(game1);

        return game1;
    }

    /**
     * Function to retrieve game by id
     *
     * @param id of the game to retrieve
     * @return @{@link Game} that has been retrieved
     */
    public Game getGameById(Long id) {
        return gameRepository.findOne(id);
    }

    /**
     * Function to retrieve the games that are joinable
     *
     * @param player @{@link Player} to look for
     * @return List of @{@link Game} to join
     */
    public List<Game> getGamesToJoin(Player player) {
        return gameRepository.findByState(Game.State.WAIT_FOR_OPPONENT)
                .stream().filter(
                        game -> game.getFirstPlayer() != player
                ).collect(Collectors.toList());
    }

    /**
     * Function to retrieve active games for player
     *
     * @param player @{@link Player} to look for
     * @return List of @{@link Game} that are active for player
     */
    public List<Game> getPlayerGames(Player player) {
        return gameRepository.findByState(Game.State.GAME_IN_PLAY)
                .stream().filter(
                        game -> (game.getFirstPlayer() == player ||
                                game.getSecondPlayer() == player)

                ).collect(Collectors.toList());
    }

}
