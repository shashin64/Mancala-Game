package com.game.project.kalahgame;

import com.game.project.kalahgame.model.Game;
import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.repository.BoardRepository;
import com.game.project.kalahgame.service.BoardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepositoryMock;

    private BoardService boardService;

    @Before
    public void init() {
        boardService = new BoardService(boardRepositoryMock);
    }

    @Test
    public void testCreateNewBoard() {
        Game gameMock = mock(Game.class);
        GameBoard result = boardService.createNewBoard(gameMock);
        assertEquals(result.getGame(), gameMock);
        verify(boardRepositoryMock, times(1)).save(any(GameBoard.class));
    }
}
