package net.espectralgames.bingoEspectral.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Unfinished
public class BingoExpansion extends PlaceholderExpansion {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();

    @Override
    public @NotNull String getIdentifier() {
        return "bingo";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MarkManFlame_55";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        final var bingoPlayer = this.bingoGame.getPlayer(player.getPlayer());

        if (bingoPlayer == null) return "";

        return switch (params) {
            //case "team_name" -> bingoPlayer.getPlayer().getName();
            //case "team_prefix" -> bingoPlayer.getTeam() == null ? "" : bingoPlayer.getTeam().getPrefix();
            //case "team_color" -> bingoPlayer.getTeam() == null ? "" : "&" + bingoPlayer.getTeam().getColor().asHexString();
            default -> "";
        };
    }

}
