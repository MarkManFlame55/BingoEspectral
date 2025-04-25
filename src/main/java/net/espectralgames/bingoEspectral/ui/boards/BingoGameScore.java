package net.espectralgames.bingoEspectral.ui.boards;

import com.google.common.base.Preconditions;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class BingoGameScore extends BukkitRunnable {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();
    private final String OBJECTIVE_NAME = "bingoScore";


    private void createScoreboard(Player player) {
        final LangConfig lang = this.plugin.getLangConfig();
        final BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
        if (bingoPlayer == null) return;
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY, TextBuilder.minimessage("<gradient:aqua:blue><b>BingoEspectral"), RenderType.INTEGER);
        objective.numberFormat(NumberFormat.blank());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        createScore("blank1", Component.text(""), 20, objective);
        createScore("playerNameTitle", TextBuilder.minimessage("<aqua>Player:"), 19, objective);
        createScore("playerName", TextBuilder.minimessage("<gray>> <white>" + player.getName()), 18, objective);
        createScore("blank2", Component.text(""), 17, objective);
        createScore("pointsTitle", TextBuilder.minimessage("<aqua>Your Points:"), 16, objective);
        createScore("playerPoints", TextBuilder.minimessage("<gray>> <white>" + bingoPlayer.getPoints()), 15, objective);
        createScore("blank3", Component.text(""), 14, objective);
        createScore("serverIP", TextBuilder.minimessage("<aqua>uhc.espectral.es"), 13, objective);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
        Preconditions.checkNotNull(objective);
        BingoPlayer bingoPlayer = this.plugin.getBingoGame().getPlayer(player);
        modifyCustomName("playerPoints", TextBuilder.minimessage("<white>" + bingoPlayer.getPoints()), objective);
    }

    @Override
    public void run() {
        for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
            final Player player = bingoPlayer.getPlayer();
            if (player.getScoreboard().getObjective(OBJECTIVE_NAME) != null) {
                updateScoreboard(player);
            } else {
                createScoreboard(player);
            }
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    private void createScore(String id, Component customName, int pos, Objective objective) {
        Score score = objective.getScore(id);
        score.customName(customName);
        score.setScore(pos);
    }

    private void modifyCustomName(String id, Component customName, Objective objective) {
        Score score = objective.getScore(id);
        score.customName(customName);
    }
    private void modifyPosition(String id, int pos, Objective objective) {
        Score score = objective.getScore(id);
        score.setScore(pos);
    }
}
