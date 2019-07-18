package com.game.project.kalahgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.project.kalahgame.controller.PlayController;
import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.repository.PlayerRepository;
import com.game.project.kalahgame.service.BoardService;
import com.game.project.kalahgame.service.GameService;
import com.game.project.kalahgame.service.PlayService;
import com.game.project.kalahgame.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PlayController.class)
public class PlayControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PlayService playService;

    @MockBean
    private SimpMessagingTemplate templateMock;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testMovePosition() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, Game.State.WAIT_FOR_OPPONENT);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        GameBoard board = new GameBoard(gameOne);
        board.setId(1); // Does not matter here

        List<Pit> pits = new ArrayList<>();

        for(int i = 1; i <= 2; i++) {
            Pit p = new Pit(board, (i*7), 0, Pit.PitType.LARGE);
            pits.add(p);
        }

        for(int i = 1; i <= 6; i++) {
            Pit p = new Pit(board, i, 6, Pit.PitType.SMALL);
            pits.add(p);
        }

        for(int i = 8; i <= 13; i++) {
            Pit p = new Pit(board, i, 6,Pit.PitType.SMALL);
            pits.add(p);
        }

        board.setPits(pits);

        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGameById(any(Long.class))).thenReturn(gameOne);
        when(playService.doMove(gameOne, player, 2)).thenReturn(board);
        this.mockMvc.perform(post("/play/move/2").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(board))));
    }

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testGetTurn() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Player playerTwo = new Player("dimitris", "dimitris");
        Long gameId = 1L;
        Game gameOne  = new Game(gameId, player, playerTwo, player, Game.State.GAME_IN_PLAY);

        // Rules
        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGameById(gameId)).thenReturn(gameOne);

        // Call SUT
        this.mockMvc.perform(get("/play/turn").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(player))));
    }

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testGetPlayerScore() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Player playerTwo = new Player("irene", "irene");
        Long gameId = 1L;
        Game gameOne  = new Game(gameId, player, playerTwo, player, Game.State.GAME_IN_PLAY);

        int score = 991;
        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGameById(gameId)).thenReturn(gameOne);
        when(playService.getScore(gameOne, player)).thenReturn(score);

        this.mockMvc.perform(get("/play/score").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(score))));
    }

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testGetGameState() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Player playerTwo = new Player("irene", "irene");
        Long gameId = 1L;
        Game gameOne  = new Game(gameId, player, playerTwo, player, Game.State.GAME_IN_PLAY);

        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGameById(gameId)).thenReturn(gameOne);

        this.mockMvc.perform(get("/play/state").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(Game.State.GAME_IN_PLAY))));
    }

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testGetBoard() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, Game.State.WAIT_FOR_OPPONENT);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        GameBoard board = new GameBoard(gameOne);
        board.setId(1);

        List<Pit> pits = new ArrayList<>();

        for(int i = 1; i <= 2; i++) {
            Pit p = new Pit(board, (i*7), 0, Pit.PitType.LARGE);
            pits.add(p);
        }

        for(int i = 1; i <= 6; i++) {
            Pit p = new Pit(board, i, 6, Pit.PitType.SMALL);
            pits.add(p);
        }

        for(int i = 8; i <= 13; i++) {
            Pit p = new Pit(board, i, 6, Pit.PitType.SMALL);
            pits.add(p);
        }

        board.setPits(pits);

        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGameById(gameId)).thenReturn(gameOne);
        when(boardService.getBoardByGame(gameOne)).thenReturn(board);
        this.mockMvc.perform(get("/play/board").sessionAttr("gameId", gameId)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(board))));
    }

}
