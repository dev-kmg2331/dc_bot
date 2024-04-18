package org.dc_bot.dc_bot.event.consumer;

import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Optional;

import static org.dc_bot.dc_bot.config.JDAConfig.FORUM_CHANNEL_NAME;

public class RaidSlashEventConsumer implements SlashEventConsumer {
    @Override
    public void consume(SlashCommandInteractionEvent event) {
        Optional<ForumChannel> optionalForumChannel = event.getGuild().getForumChannels().stream().filter(channel -> channel.getName().equals(FORUM_CHANNEL_NAME)).findFirst();

        // 포럼 채널 없다면 빈 배열 리턴
        if (optionalForumChannel.isEmpty()) event.reply("no forum channel").queue();

        ForumChannel forumChannel = optionalForumChannel.get();

//        forumChannel.createThreadChannel();
    }
}
