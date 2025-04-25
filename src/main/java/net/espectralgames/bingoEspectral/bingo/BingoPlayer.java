package net.espectralgames.bingoEspectral.bingo;

import com.google.common.base.Preconditions;
import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BingoPlayer {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();

    private final Player player;
    private BingoGame game;
    private BingoCard personalCard;
    private @Nullable BingoTeam team;
    private int points = 0;

    public BingoPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BingoGame getGame() {
        return game;
    }

    public void setGame(BingoGame game) {
        this.game = game;
    }
    public BingoCard getPersonalCard() {
        return this.personalCard;
    }

    public void setPersonalCard(BingoCard personalCard) {
        this.personalCard = personalCard;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BingoPlayer bingoPlayer) {
            return bingoPlayer.getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
        }
        return false;
    }

    public @Nullable BingoTeam getTeam() {
        return team;
    }

    public void setTeam(@Nullable BingoTeam team) {
        this.team = team;
    }

    public void sendMessage(Component message) {
        this.player.sendMessage(message);
    }

    public void sendTeamMessage(String message) {
        if (this.team != null) {
            final LangConfig lang = this.plugin.getLangConfig();
            String team_message = lang.team("message");
            Preconditions.checkNotNull(team_message, "bingo.team.message is null!");
            this.team.sendMessage(
                    TextBuilder.minimessage(team_message
                            .replace("%team_prefix", this.team.getPrefix())
                            .replace("%player%", this.player.getName())
                            .replace("%message%", message)));
        }
    }

    public String getName() {
        return this.getPlayer().getName();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
