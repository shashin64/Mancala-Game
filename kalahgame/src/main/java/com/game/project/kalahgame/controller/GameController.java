package com.game.project.kalahgame.controller;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Class to handle @{@link Game} related REST calls
 */
@RestController
@RequestMapping("/game")
public class GameController {

    private GameService gameService;
    private PlayerService playerService;
    private BoardService boardService;
    private PitService pitService;
    private HttpSession httpSession;
    private SimpMessagingTemplate template;



    private final Logger logger = LoggerFactory.getLogger(GameController.class);


    /**
     * GameController constructor
     *
     * @param gameService @{@link GameService} dependency
     * @param playerService @{@link PlayerService} dependency
     * @param boardService @{@link BoardService} dependency
     * @param pitService @{@link PitService} dependency
     * @param httpSession @{@link HttpSession} dependency
     */
    @Autowired
    public GameController(GameService gameService, PlayerService playerService, BoardService boardService,
                          PitService pitService, HttpSession httpSession,SimpMessagingTemplate template) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.boardService = boardService;
        this.pitService = pitService;
        this.httpSession = httpSession;
        this.template = template;
      ;
    }

    /**
     * REST endpoint to create a new Game
     *
     * @return @{@link Game} instance of the newly created game.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Game createNewGame() {
        logger.debug("Creating new game");

        Player player = playerService.getLoggedInUser();
        Game game = gameService.createNewGame(player);

        // Create the game GameBoard
        GameBoard board = boardService.createNewBoard(game);

        // Create Pits 6x6 layout + 2 Large Pits
        pitService.createPit(board, Pit.PitType.LARGE, PlayService.P1_STORE, 0); // store pos 7
        pitService.createPit(board, Pit.PitType.LARGE, PlayService.P2_STORE, 0); // store post 14

        // P1 small pits
        for (int i = PlayService.P1_LOWER_BOUNDARY; i <= PlayService.P1_UPPER_BOUNDARY; i++) {
            pitService.createPit(board, Pit.PitType.SMALL, i, 6);
        }

        // P2 small pits
        for (int i = PlayService.P2_LOWER_BOUNDARY; i <= PlayService.P2_UPPER_BOUNDARY; i++) {
            pitService.createPit(board, Pit.PitType.SMALL, i, 6);
        }

        httpSession.setAttribute("gameId", game.getId());
        logger.debug("new game id: " + httpSession.getAttribute("gameId")+ " stored in session" );

        // Notify players in Lobby of update over socket
       template.convertAndSend("/update/lobby", "update");

        return game;
    }

    /**
     * REST endpoint to join a existing Game
     *
     * @param id of the game to join
     * @return @{@link Game} just joined
     */
    @RequestMapping(value = "/join/{id}", method = RequestMethod.POST)
    public Game joinGame(@PathVariable Long id) {
        logger.debug("Joining game");

        // Get logged in player
        Player player = playerService.getLoggedInUser();

        // Retrieve game to join and join
        Game game = gameService.joinGame(player, id);

        // Store game id in HttpSession
        httpSession.setAttribute("gameId", id);

        // Notify lobby and game of status update over socket
        template.convertAndSend("/update/join/" + id, "joined");
        template.convertAndSend("/update/lobby", "updated");

        logger.debug("existing game id: " + httpSession.getAttribute("gameId")+ " stored in session" );

        return game;
    }

    /**
     * REST endpoint to get games to join
     *
     * @return List of @{@link Game} able to join
     */
    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Game> getGamesToJoin() {
        logger.debug("Getting games to Join");

        return gameService.getGamesToJoin(playerService.getLoggedInUser());
    }


    /**
     * REST endpoint to get current active games for player
     *
     * @return List of @{@link Game} that are active for player
     */
    @RequestMapping(value = "/player/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Game> getPlayerGames() {
        logger.debug("Getting active game for player");

        return gameService.getPlayerGames(playerService.getLoggedInUser());
    }

    /**
     * REST endpoint to switch to game by id
     *
     * @param id of the game to retrieve
     * @return @{@link Game} of the game to switch to
     */
    @RequestMapping(value = "/{id}")
    public Game getGameProperties(@PathVariable Long id) {
        logger.debug("Switching to game");

        // Set game id in HttpSession
        httpSession.setAttribute("gameId", id);

        logger.debug("existing game id: " + httpSession.getAttribute("gameId")+ " stored in session" );

        return gameService.getGameById(id);
    }
}