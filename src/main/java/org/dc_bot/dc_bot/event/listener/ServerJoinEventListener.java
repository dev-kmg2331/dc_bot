package org.dc_bot.dc_bot.event.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.dc_bot.dc_bot.event.enums.CommandName;
import org.dc_bot.dc_bot.event.enums.OptionName;
import org.dc_bot.dc_bot.util.DateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.dc_bot.dc_bot.config.JDAConfig.FORUM_CHANNEL_NAME;

@Slf4j
public class ServerJoinEventListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        log.info("joined server : {}", event.getGuild().getName());
        event.getGuild()
                .updateCommands()
                .addCommands(
                        // 레이드 공지 등록 커맨드.
                        Commands.slash(CommandName.RAID.val(), "레이드 공지 등록")
                                .addOptions(
                                        // 날짜 옵션. 자동완성 이벤트를 통해 현재 날짜와 비교하여 옵션을 가져온다.
                                        new OptionData(OptionType.STRING, OptionName.DATE.val(), "날짜 선택").setAutoComplete(true),
                                        // 시간 옵션. 0시 ~ 24시까지.
                                        new OptionData(OptionType.STRING, OptionName.TIME.val(), "시간 선택")
                                                .addChoices(
                                                        IntStream.rangeClosed(0, 24)
                                                                .mapToObj(String::valueOf)
                                                                .map(i -> new Command.Choice(i, i))
                                                                .toList()
                                                ),
                                        // 분 옵션. 0분부터 60분까지 10분 단위.
                                        new OptionData(OptionType.STRING, OptionName.MIN.val(), "분 선택").addChoices(
                                                IntStream.rangeClosed(0, 6)
                                                        .mapToObj(i -> i * 10)
                                                        .map(String::valueOf)
                                                        .map(i -> new Command.Choice(i, i))
                                                        .toList()
                                        ),
                                        // 컨텐츠 선택 옵션. 서버 기본 설정인 /setup 명령어로 포럼 채널을 먼저 생성하여야 함.
                                        new OptionData(OptionType.STRING, OptionName.CONTENT.val(), "컨텐츠 선택. 서버 기본 설정을 하지 않았다면 정상적으로 동작하지 않음.").setAutoComplete(true),
                                        // 컨텐츠 난이도 선택 옵션.
                                        new OptionData(OptionType.STRING, OptionName.DIFFICULTY.val(), "난이도 선택")
                                                .addChoices(
                                                        Stream.of("노말", "하드", "헬")
                                                                .map(s -> new Command.Choice(s, s))
                                                                .toList()
                                                )
                                ),
                        // 서버 기본 설정 커맨드.
                        Commands.slash(CommandName.SETUP.val(), "set default server settings")
                )
                .queue();
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        CommandName command = CommandName.value(event.getName());
        String optionName = event.getFocusedOption().getName();

        if (command == null) return;

        switch (command) {
            case RAID -> {
                // 시간 설정 자동완성
                if (OptionName.DATE.val().equals(optionName)) {
                    List<LocalDateTime> dates = new ArrayList<>();

                    for (int i = 0; i < 7; i++) dates.add(LocalDateTime.now().plusDays(i));

                    event.replyChoices(
                            dates.stream()
                                    .map(date -> new Command.Choice(
                                            DateUtil.convertToKR(date),
                                            DateUtil.convertDefault(date))
                                    )
                                    .toList()
                    ).queue();
                    // 컨텐츠 설정 자동완성
                } else if (OptionName.CONTENT.val().equals(optionName)) {
                    Optional<ForumChannel> forumChannel = event.getGuild().getForumChannels().stream().filter(channel -> channel.getName().equals(FORUM_CHANNEL_NAME)).findFirst();

                    // 포럼 채널 없다면 빈 배열 리턴
                    if (forumChannel.isEmpty()) event.replyChoices(new ArrayList<>()).queue();
                    else {
                        event.replyChoices(
                                forumChannel.get().getAvailableTags()
                                        .stream()
                                        .map(tag -> new Command.Choice(tag.getName(), tag.getId()))
                                        .toList()
                        ).queue();
                    }
                }
            }
            case SETUP -> {

            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        log.info("server : {}, command : {}", event.getGuild().getName(), event.getName());
        String commandLine = event.getName();
        CommandName command = CommandName.value(commandLine);

        if (command == null) {
            log.info("unsupported slash command : {}", commandLine);
            event.reply("지원되지 않는 명령어입니다.").queue();
            return;
        }

        // 각 CommandName enum 마다 고유 consumer class 존재
        command.getConsumer().consume(event);
    }
}
