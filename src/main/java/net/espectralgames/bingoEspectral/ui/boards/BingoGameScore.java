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
        final Objective objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY, TextBuilder.minimessage(lang.score("score_title")), RenderType.INTEGER);
        objective.numberFormat(NumberFormat.blank());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        createScore("blank1", Component.text(""), 10, objective);
        createScore("playerNameTitle", TextBuilder.minimessage(lang.score("player_name_title")), 9, objective);
        createScore("playerName", TextBuilder.minimessage(player.getName()), 8, objective);
        createScore("blank2", Component.text(""), 7, objective);
        createScore("teamNameTitle", TextBuilder.minimessage(lang.score("team_title")), 6, objective);
        createScore("teamName", TextBuilder.minimessage(bingoPlayer.getTeam().getTeamName()).color(bingoPlayer.getTeam().getColor()), 5, objective);
        createScore("blank4", Component.text(""), 4, objective);
        createScore("pointsTitle", TextBuilder.minimessage(lang.score("points_title")), 3, objective);
        createScore("playerPoints", TextBuilder.minimessage("" + bingoPlayer.getPoints()), 2, objective);
        createScore("blank3", Component.text(""), 1, objective);
        createScore("serverIP", TextBuilder.minimessage(lang.score("server_ip_title")), 0, objective);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboard(Player player) {
        final LangConfig lang = this.plugin.getLangConfig();
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
        Preconditions.checkNotNull(objective);
        BingoPlayer bingoPlayer = this.plugin.getBingoGame().getPlayer(player);

        objective.displayName(TextBuilder.minimessage(lang.score("score_title")));
        modifyCustomName("playerNameTitle", TextBuilder.minimessage(lang.score("player_name_title")), objective);
        modifyCustomName("teamNameTitle", TextBuilder.minimessage(lang.score("team_title")), objective );
        modifyCustomName("pointsTitle", TextBuilder.minimessage(lang.score("points_title")), objective);
        modifyCustomName("serverIP", TextBuilder.minimessage(lang.score("server_ip_title")), objective);
        modifyCustomName("playerPoints", TextBuilder.minimessage("" + bingoPlayer.getTeam().getTeamPoints()), objective);
        modifyCustomName("teamName",TextBuilder.minimessage(bingoPlayer.getTeam().getTeamName()).color(bingoPlayer.getTeam().getColor()), objective);
    }

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.bingoGame.getPlayer(player) == null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }

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
