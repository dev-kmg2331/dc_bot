package org.dc_bot.dc_bot.event.enums;

public enum OptionName {
    DATE("날짜"), TIME("시간"), MIN("분"),
    CONTENT("컨텐츠"), DIFFICULTY("난이도");

    private final String val;

    OptionName(String val) {
        this.val = val;
    }

    public String val() {
        return this.val;
    }
}
