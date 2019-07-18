package com.game.project.kalahgame;

import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.repository.PlayerRepository;
import com.game.project.kalahgame.service.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepositoryMock;

    private PlayerService playerService;

    @Before
    public void init() {
        playerService = new PlayerService(playerRepositoryMock);
    }

    @Test
    public void testGetPlayerByName() {
        Player player = mock(Player.class);
        String name = "shashin";
        when(playerRepositoryMock.findOneByUsername(name)).thenReturn(player);
        Player result = playerService.getPlayerByUsername(name);
        assertEquals(player, result);
    }

}
