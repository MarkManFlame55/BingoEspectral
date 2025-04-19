package net.espectralgames.bingoEspectral.bingo.team;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BingoTeam {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();

    private BingoPlayer owner;
    private final List<BingoPlayer> members;
    private String prefix;
    private TextColor color;
    private String teamName;

    public BingoTeam(BingoPlayer owner, String teamName, String prefix, TextColor color) {
        this.owner = owner;
        this.teamName = teamName;
        this.prefix = prefix;
        this.color = color;
        this.members = new ArrayList<>();
    }


    public void sendMessage(Component message) {
        for (BingoPlayer bingoPlayer : this.members) {
            bingoPlayer.sendMessage(message);
        }
    }

    public boolean addMember(Player player) {
        final BingoPlayer bingoPlayer = new BingoPlayer(player);

        if (this.members.contains(bingoPlayer)) return false;

        final LangConfig lang = this.plugin.getLangConfig();

        this.members.add(bingoPlayer);
        this.bingoGame.addPlayer(bingoPlayer);
        bingoPlayer.setTeam(this);
        sendMessage(TextBuilder.info(lang.team("player_join")));

        return true;
    }

    public boolean removeMember(BingoPlayer bingoPlayer) {
        if (this.members.remove(bingoPlayer)) {
            final LangConfig lang = this.plugin.getLangConfig();

            if (!this.bingoGame.isStarted()) {
                this.bingoGame.removePlayer(bingoPlayer);
            }

            bingoPlayer.setTeam(null);
            sendMessage(TextBuilder.info(lang.team("player_leave")));
            return true;
        }
        return false;
    }


    public List<BingoPlayer> getMembers() {
        return members;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public BingoPlayer getOwner() {
        return owner;
    }

    public void setOwner(BingoPlayer owner) {
        this.owner = owner;
    }
}
