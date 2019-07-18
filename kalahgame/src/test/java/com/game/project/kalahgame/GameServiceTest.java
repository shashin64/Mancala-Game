package com.game.project.kalahgame;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.repository.GameRepository;
import com.game.project.kalahgame.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepositoryMock;

    private GameService gameService;

    @Before
    public void init() {
        gameService = new GameService(gameRepositoryMock);
    }

    @Test
    public void testCreateNewGame() {
        Player playerMock = mock(Player.class);

        Game result = gameService.createNewGame(playerMock);

        assertEquals(result.getFirstPlayer(), playerMock);
        assertEquals(result.getPlayerTurn(), playerMock);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    private void assertEquals(Player firstPlayer, Player playerMock) {
    }

    @Test
    public void testJoinGame() {
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = new Game(gameId, playerOneMock, null, playerOneMock, Game.State.WAIT_FOR_OPPONENT);
        when(gameRepositoryMock.findOne(gameId)).thenReturn(game);

        Game result = gameService.joinGame(playerMock, gameId);

        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerTurn(), playerOneMock);
        assertEquals(result.getSecondPlayer(), playerMock);

        verify(gameRepositoryMock, times(2)).save(any(Game.class)); // Also for updateGameState
    }

    @Test
    public void testSwitchTurn() {
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = new Game(gameId, playerOneMock, playerMock, playerMock, Game.State.GAME_IN_PLAY);

        when(gameRepositoryMock.findOne(gameId)).thenReturn(game);

        Game result = gameService.switchTurn(playerMock, gameId);

        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerTurn(), playerMock);
        assertEquals(result.getSecondPlayer(), playerMock);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    @Test
    public void testUpdateGameState() {
        Player playerMock = mock(Player.class);
        Player playerOneMock = mock(Player.class);
        Long gameId = 1L;
        Game game = new Game(gameId, playerOneMock, playerMock, playerOneMock, Game.State.GAME_IN_PLAY);

        when(gameRepositoryMock.findOne(gameId)).thenReturn(game);

        Game result = gameService.updateGameState(game, Game.State.GAME_OVER);

        assertEquals(result.getFirstPlayer(), playerOneMock);
        assertEquals(result.getPlayerTurn(), playerOneMock);
        assertEquals(result.getSecondPlayer(), playerMock);

        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }
}
