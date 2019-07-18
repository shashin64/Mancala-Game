package com.game.project.kalahgame;

import com.game.project.kalahgame.model.GameBoard;
import com.game.project.kalahgame.model.Pit;
import com.game.project.kalahgame.repository.PitRepository;
import com.game.project.kalahgame.service.PitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PitServiceTest {

    @Mock
    private PitRepository pitRepositoryMock;

    private PitService pitService;

    @Before
    public void init() {
        pitService = new PitService(pitRepositoryMock);
    }

    @Test
    public void testCreatePitStore() {
        GameBoard boardMock = mock(GameBoard.class);
        Pit.PitType type = Pit.PitType.LARGE;
        int position = 7;
        int nrOfStones = 10;

        Pit result = pitService.createPit(boardMock, type, position, nrOfStones);

        assertEquals(result.getGameBoard(), boardMock);
        assertEquals(result.getPitType(), type);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getStoneCount(), nrOfStones);

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }

    @Test
    public void testCreatePitHouse() {
        GameBoard boardMock = mock(GameBoard.class);
        Pit.PitType type = Pit.PitType.SMALL;
        int position = 10;
        int nr_of_stones = 4;

        Pit result = pitService.createPit(boardMock, type, position, nr_of_stones);
        assertEquals(result.getGameBoard(), boardMock);
        assertEquals(result.getPitType(), type);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getStoneCount(), nr_of_stones);

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }


    @Test
    public void testUpdateNumberOfStonesByAmount() {
        GameBoard boardMock = mock(GameBoard.class);
        int position = 10;
        int amount = 4;

        Pit pit = new Pit(boardMock, position, 100, Pit.PitType.SMALL);
        when(pitRepositoryMock.findByGameBoardAndPosition(boardMock, position)).thenReturn(pit);

        Pit result = pitService.updatePitNumberOfStonesByAmount(boardMock, position, amount);
        assertEquals(result.getGameBoard(), boardMock);
        assertEquals(result.getPosition(), position);
        assertEquals(result.getStoneCount(), 104);

        verify(pitRepositoryMock, times(1)).save(any(Pit.class));
    }
}
