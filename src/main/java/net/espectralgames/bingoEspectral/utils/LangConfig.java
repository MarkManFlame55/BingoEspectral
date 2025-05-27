package net.espectralgames.bingoEspectral.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class LangConfig {

    private final YamlConfiguration config;

    public LangConfig(File file) {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String error(@NotNull String key) {
        return this.getString("bingo.error." + key);
    }

    public String team(@NotNull String key) {
        return this.getString("bingo.team." + key);
    }

    public String game(@NotNull String key) {
        return this.getString("bingo.game." + key);
    }

    public String score(@NotNull String key) {
        return this.getString("bingo.score." + key);
    }

    public String ui(@NotNull String key) {
        return this.getString("bingo.ui." + key);
    }

    public String help(@NotNull String key) {
        return this.getString("bingo.help." + key);
    }

    public String config(@NotNull String key) {
        return this.getString("bingo.config." + key);
    }

    public String getString(@NotNull String key) {
        return this.config.getString(key);
    }
}
