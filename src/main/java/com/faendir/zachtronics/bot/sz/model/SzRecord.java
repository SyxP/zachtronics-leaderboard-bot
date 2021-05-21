package com.faendir.zachtronics.bot.sz.model;

import com.faendir.zachtronics.bot.model.Record;
import kotlinx.serialization.Serializable;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Serializable
@Value
public class SzRecord implements Record {
    SzScore score;
    String author;
    String link;

    @NotNull
    @Override
    public String toDisplayString() {
        return score.toDisplayString() + (author == null ? "" : " by " + author) + " " + link;
    }
}