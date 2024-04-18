package org.dc_bot.dc_bot.event.consumer;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface EventConsumer<T extends Event> {
    void consume(T event);
}
