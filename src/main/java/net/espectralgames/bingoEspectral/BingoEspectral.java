package net.espectralgames.bingoEspectral;

import io.papermc.paper.plugin.configuration.PluginMeta;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.events.BingoEvents;
import net.espectralgames.bingoEspectral.bingo.team.TeamChatManager;
import net.espectralgames.bingoEspectral.commands.bingoCommand;
import net.espectralgames.bingoEspectral.commands.bingocardCommand;
import net.espectralgames.bingoEspectral.commands.bingoteamCommand;
import net.espectralgames.bingoEspectral.expansion.BingoExpansion;
import net.espectralgames.bingoEspectral.item.BingoCardItem;
import net.espectralgames.bingoEspectral.ui.boards.BingoGameScore;
import net.espectralgames.bingoEspectral.ui.listeners.MenuListeners;
import net.espectralgames.bingoEspectral.utils.LangConfig;
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

import java.io.File;
import java.io.IOException;

public final class BingoEspectral extends JavaPlugin {


    private static BingoEspectral plugin;
    private BingoGame bingoGame;
    private File cardsFile, langFile;
    private YamlConfiguration cardsConfig = new YamlConfiguration();
    private LangConfig langConfig;
    private BingoExpansion expansion;


    @Override
    public void onEnable() {
        plugin = this;
        bingoGame = new BingoGame();
        expansion = new BingoExpansion();
        expansion.register();

        saveDefaultConfig();

        createFiles();

        createTeams();

        getCommand("bingo").setExecutor(new bingoCommand());
        getCommand("bingocard").setExecutor(new bingocardCommand());
        getCommand("bingoteam").setExecutor(new bingoteamCommand());

        getServer().getPluginManager().registerEvents(new BingoEvents(), this);
        getServer().getPluginManager().registerEvents(new MenuListeners(), this);
        getServer().getPluginManager().registerEvents(new BingoCardItem(), this);
        getServer().getPluginManager().registerEvents(new TeamChatManager(), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (bingoGame.isWaitingToStart()) {
                    bingoGame.waitingSequence(BingoEspectral.this);
                }
            }
        }.runTaskTimer(this, 0, 20);

        new BingoGameScore().runTaskTimer(this, 0, 20);
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
            langConfig = new LangConfig(langFile);
            this.getComponentLogger().info("lang.yml configuration loaded!");
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

    public LangConfig getLangConfig() {
        return langConfig;
    }

    public void reloadCardsConfig() {
        this.cardsConfig = YamlConfiguration.loadConfiguration(cardsFile);
        this.getLogger().info("cards.yml configuration reloaded!");
    }

    public void reloadLangConfig() {
        this.langConfig = new LangConfig(langFile);
        this.getLogger().info("lang.yml configuration reloaded!");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.getLogger().info("config.yml configuration reloaded!");
    }
}
