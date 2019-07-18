package com.game.project.kalahgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.project.kalahgame.controller.GameController;
import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.repository.PlayerRepository;
import com.game.project.kalahgame.service.BoardService;
import com.game.project.kalahgame.service.GameService;
import com.game.project.kalahgame.service.PitService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PitService pitService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private SimpMessagingTemplate templateMock;

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testListGamesToJoin() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, Game.State.WAIT_FOR_OPPONENT);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getGamesToJoin(player)).thenReturn(games);

        this.mockMvc.perform(get("/game/list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(games))));
    }

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testListGamesActive() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Player playerTwo = new Player("dimitris","dimitris");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, playerTwo, player, Game.State.GAME_IN_PLAY);
        List<Game> games = new ArrayList<>();
        games.add(gameOne);

        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.getPlayerGames(player)).thenReturn(games);

        this.mockMvc.perform(get("/game/player/list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(games))));
    }

    @Test
    @WithMockUser(username = "shashin", password = "shashin")
    public void testSelectGame() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Player playerTwo = new Player("dimitris","dimitris");

        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, playerTwo, player, Game.State.GAME_IN_PLAY);

        when(gameService.getGameById(gameId)).thenReturn(gameOne);

        this.mockMvc.perform(get("/game/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(gameOne))));
    }

    @Test
    @WithMockUser(username = "dimitris", password = "dimitris")
    public void testJoinGame() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin",  "shashin");
        Player playerTwo = new Player("dimitris", "dimitris");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, playerTwo, player, Game.State.GAME_IN_PLAY);

        when(playerService.getLoggedInUser()).thenReturn(playerTwo);
        when(gameService.joinGame(playerTwo, gameId)).thenReturn(gameOne);
        this.mockMvc.perform(post("/game/join/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(gameOne))));
    }

    @Test
    @WithMockUser(username = "irene", password = "irene")
    public void testCreateGame() throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        Player player = new Player("shashin", "shashin");
        Long gameId = 1L;
        Game gameOne = new Game(gameId, player, null, player, Game.State.WAIT_FOR_OPPONENT);

        GameBoard boardMock = mock(GameBoard.class);

        when(playerService.getLoggedInUser()).thenReturn(player);
        when(gameService.createNewGame(player)).thenReturn(gameOne);
        when(boardService.createNewBoard(gameOne)).thenReturn(boardMock);

        this.mockMvc.perform(post("/game/create")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(objMapper.writeValueAsString(gameOne))));

        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.LARGE), eq(7), eq(0));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.LARGE), eq(14), eq(0));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(1), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(2), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(3), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(4), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(5), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(6), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(8), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(9), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(10), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(11), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(12), eq(6));
        verify(pitService, times(1)).createPit(eq(boardMock), eq(Pit.PitType.SMALL), eq(13), eq(6));

    }
}