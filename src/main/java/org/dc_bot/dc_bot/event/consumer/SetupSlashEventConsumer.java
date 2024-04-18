package org.dc_bot.dc_bot.event.consumer;

import net.dv8tion.jda.api.entities.channel.forums.ForumTagData;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;
import java.util.Objects;

import static org.dc_bot.dc_bot.config.JDAConfig.FORUM_CHANNEL_NAME;

public class SetupSlashEventConsumer implements SlashEventConsumer {
    @Override
    public void consume(SlashCommandInteractionEvent event) {

        Objects.requireNonNull(event.getGuild())
                .getChannels()
                .stream()
                .filter(c -> c.getName().equals(FORUM_CHANNEL_NAME))
                .findFirst()
                .ifPresent(c -> c.delete().queue());

        event.getGuild()
                .createForumChannel(FORUM_CHANNEL_NAME)
                .setTopic("this is a test forum channel")
                .setAvailableTags(
                        List.of(
                                new ForumTagData("tag_test_1").setEmoji(Emoji.fromUnicode("\uD83D\uDE01")),
                                new ForumTagData("tag_test_2").setEmoji(Emoji.fromUnicode("\uD83D\uDE01")),
                                new ForumTagData("tag_test_3").setEmoji(Emoji.fromUnicode("\uD83D\uDE01"))
                        )
                )
                .onErrorFlatMap(throwable -> {
                    event.reply("cannot create forum channel on this server.").queue();
                    return null;
                })
                .onSuccess(forumChannel -> event.reply(String.format("forum channel '%s' created.", forumChannel.getName())).queue())
                .queue();
    }
}
