package com.game.project.kalahgame;

import com.game.project.kalahgame.model.Player;
import com.game.project.kalahgame.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = {"com.game.project.kalahgame.model"})
public class KalahgameApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalahgameApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PlayerRepository playerRepository) {
		return (args) -> {

			// Create a couple of players
			playerRepository.save(new Player("shashin", new BCryptPasswordEncoder().encode("shashin")));
			playerRepository.save(new Player("dimitris",  new BCryptPasswordEncoder().encode("dimitris")));

		};
	}

}
