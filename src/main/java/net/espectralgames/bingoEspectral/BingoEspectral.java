package net.espectralgames.bingoEspectral;

import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.events.BingoEvents;
import net.espectralgames.bingoEspectral.commands.bingoCommand;
import net.espectralgames.bingoEspectral.commands.bingocardCommand;
import net.espectralgames.bingoEspectral.item.BingoCardItem;
import net.espectralgames.bingoEspectral.ui.listeners.MenuListeners;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import javax.swing.event.TableModelListener;
import java.io.File;
import java.io.IOException;

public final class BingoEspectral extends JavaPlugin {

    private static BingoEspectral plugin;
    private BingoGame bingoGame;
    private File cardsFile, langFile;
    private YamlConfiguration cardsConfig = new YamlConfiguration();
    private YamlConfiguration langConfig = new YamlConfiguration();


    @Override
    public void onEnable() {
        plugin = this;
        bingoGame = new BingoGame();

        saveDefaultConfig();

        createFiles();

        createTeams();

        getCommand("bingo").setExecutor(new bingoCommand());
        getCommand("bingocard").setExecutor(new bingocardCommand());

        getServer().getPluginManager().registerEvents(new BingoEvents(), this);
        getServer().getPluginManager().registerEvents(new MenuListeners(), this);
        getServer().getPluginManager().registerEvents(new BingoCardItem(), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (bingoGame.isWaitingToStart()) {
                    bingoGame.waitingSequence();
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BingoGame getBingoGame() {
        return bingoGame;
    }

    public static BingoEspectral getPlugin() {
        return plugin;
    }

    private void createFiles() {
        cardsFile = new File(this.getDataFolder(), "cards.yml");
        langFile = new File(this.getDataFolder(), "lang.yml");

        if (!cardsFile.exists()) {
            if (cardsFile.getParentFile().mkdirs()) this.getLogger().info("cards.yml created!");
            saveResource("cards.yml", false);
        }

        if (!langFile.exists()) {
            if (langFile.getParentFile().mkdirs()) this.getLogger().info("lang.yml created!");
            saveResource("lang.yml", false);
        }

        try {
            cardsConfig.load(cardsFile);
            this.getLogger().info("cards.yml configuration loaded!");
            langConfig.load(langFile);
            this.getLogger().info("lang.yml configuration loaded!");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getCardsConfig() {
        return cardsConfig;
    }

    public void createTeams() {

        String teamName = this.getConfig().getString("spectator-team-name");
        if (teamName != null) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard score = manager.getMainScoreboard();
            if (score.getTeam(teamName) == null) {
                Team spec = score.registerNewTeam(teamName);
                spec.color(NamedTextColor.GRAY);
                spec.suffix(TextBuilder.minimessage("<gray> ☠"));
            } else {
                this.bingoGame.setSpectatorTeam(score.getTeam(teamName));
            }
        } else {
            getLogger().severe("Couldn´t load 'spectator-team-name' parameter in config.yml");
        }

    }

    public YamlConfiguration getLangConfig() {
        return langConfig;
    }

    public void reloadCardsConfig() {
        this.cardsConfig = YamlConfiguration.loadConfiguration(cardsFile);
        this.getLogger().info("cards.yml configuration reloaded!");
    }

    public void reloadLangConfig() {
        this.langConfig = YamlConfiguration.loadConfiguration(langFile);
        this.getLogger().info("lang.yml configuration reloaded!");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.getLogger().info("config.yml configuration reloaded!");
    }
}
