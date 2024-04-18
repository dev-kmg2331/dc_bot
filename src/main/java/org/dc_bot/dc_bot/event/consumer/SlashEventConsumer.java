package org.dc_bot.dc_bot.event.consumer;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashEventConsumer extends EventConsumer<SlashCommandInteractionEvent> {

    void consume(SlashCommandInteractionEvent event);
}
