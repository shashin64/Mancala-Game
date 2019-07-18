package com.game.project.kalahgame.model;

import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Class for custom player context
 */
public class CustomPlayer extends org.springframework.security.core.userdetails.User {

    private final Player player;

    public CustomPlayer(Player player) {
        super(player.getUsername(),
                player.getPassword(),
                true,
                true,
                true,
                true,
                ImmutableSet.of(new SimpleGrantedAuthority("create")));

        this.player = player;
    }

    public Player getPlayer() {
        return  player;
    }
}