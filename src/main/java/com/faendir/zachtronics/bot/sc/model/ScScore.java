package com.faendir.zachtronics.bot.sc.model;

import com.faendir.zachtronics.bot.model.Score;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ScScore implements Score {
    /** it's also unbeatable */
    public static final ScScore INVALID_SCORE = new ScScore(-1, -1, -1);

    private final int cycles;
    private final int reactors;
    private final int symbols;

    private boolean bugged = true;
    private boolean precognitive = true;

    /** ccc/r/ss[/BP] */
    @NotNull
    @Override
    public String toDisplayString() {
        String result = cycles + "/" + reactors + "/" + symbols;
        if (bugged || precognitive) {
            result += "/";
            if (bugged) result += "B";
            if (precognitive) result += "P";
        }
        return result;
    }

    /** ccc/r/ss */
    public static final Pattern REGEX_SIMPLE_SCORE = Pattern.compile(
            "\\**(?<cycles>[\\d,]+)\\**(?<oldRNG>\\\\\\*)?[/-]\\**(?<reactors>\\d+)\\**[/-]\\**(?<symbols>\\d+)\\**");

    /** we assume m matches */
    @NotNull
    public static ScScore parseSimpleScore(@NotNull Matcher m) {
        int cycles = Integer.parseInt(m.group("cycles").replace(",", ""));
        int reactors = Integer.parseInt(m.group("reactors"));
        int symbols = Integer.parseInt(m.group("symbols"));
        return new ScScore(cycles, reactors, symbols);
    }

    /** ccc/r/ss[/BP] */
    public static final Pattern REGEX_BP_SCORE = Pattern
            .compile(REGEX_SIMPLE_SCORE + "(?:[/-](?<flags>B?P?))?", Pattern.CASE_INSENSITIVE);

    @Nullable
    public static ScScore parseBPScore(@NotNull String string) {
        Matcher m = REGEX_BP_SCORE.matcher(string);
        return m.matches() ? parseBPScore(m) : null;
    }

    /** we assume m matches */
    @NotNull
    public static ScScore parseBPScore(@NotNull Matcher m) {
        ScScore score = parseSimpleScore(m);
        String flags = m.group("flags");
        if (flags == null)
            flags = "";
        score.setBugged(flags.toUpperCase().contains("B"));
        score.setPrecognitive(flags.toUpperCase().contains("P"));

        return score;
    }
}
