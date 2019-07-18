package com.game.project.kalahgame.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Class for the configuration of WebSocket Security
 */
@Configuration
public class WebSocketSecurityConfig
        extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/hoi/**").authenticated()
                .simpSubscribeDestMatchers("/moved/**").authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}