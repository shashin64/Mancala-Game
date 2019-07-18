package com.game.project.kalahgame;
import static org.assertj.core.api.Assertions.assertThat;

import com.game.project.kalahgame.controller.GameController;
import com.game.project.kalahgame.controller.PlayController;
import com.game.project.kalahgame.controller.PlayerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KalahgameApplicationTests {

	@Autowired
	private GameController gameController;

	@Autowired
	private PlayController playController;

	@Autowired
	private PlayerController playerController;

	@Test
	public void contextLoads() {
		assertThat(gameController).isNotNull();
		assertThat(playController).isNotNull();
		assertThat(playerController).isNotNull();

	}


}
