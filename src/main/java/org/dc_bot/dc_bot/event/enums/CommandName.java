package org.dc_bot.dc_bot.event.enums;

import org.dc_bot.dc_bot.event.consumer.RaidSlashEventConsumer;
import org.dc_bot.dc_bot.event.consumer.SlashEventConsumer;
import org.dc_bot.dc_bot.event.consumer.EventConsumerFactory;
import org.dc_bot.dc_bot.event.consumer.SetupSlashEventConsumer;

public enum CommandName implements EventConsumerFactory {
    RAID("레이드"),
    SETUP("setup");

    private final String val;


    CommandName(String val) {
        this.val = val;
    }

    public String val() {
        return this.val;
    }

    /**
     * enum 마다 고유 consumer class 존재
     * @see SlashEventConsumer
     * */
    @Override
    public SlashEventConsumer getConsumer() {
        return switch (this) {
            case SETUP -> new SetupSlashEventConsumer();
            case RAID -> new RaidSlashEventConsumer();
            default -> throw new RuntimeException("consumer not defined");
        };
    }

    public static CommandName value(String name) {
        if (RAID.val().equals(name)) return CommandName.RAID;
        else if (SETUP.val().equals(name)) return CommandName.SETUP;
        else return null;
    }
}
