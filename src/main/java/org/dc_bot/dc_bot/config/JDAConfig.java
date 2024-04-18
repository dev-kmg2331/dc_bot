package org.dc_bot.dc_bot.config;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.dc_bot.dc_bot.event.listener.ServerJoinEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class JDAConfig {
    private final Environment environment;

    public static final String FORUM_CHANNEL_NAME = "forum_channel_test";

    @Bean
    public JDA jdaInstance() {
        String token = environment.getProperty("token.discord-bot-token");

        if (token == null) throw new RuntimeException("require 'token.discord-bot-token' property in application.yml");

        return JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES
                )
                .setActivity(Activity.playing("메시지 대기중..."))
                .addEventListeners(
                    new ServerJoinEventListener()
                )
                .build();
    }
}
