package net.espectralgames.bingoEspectral.bingo.team;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final List<BingoTeam> teams = new ArrayList<>();

    public BingoTeam getTeam(String teamName) {
        for (BingoTeam bingoTeam : teams) {
            if (bingoTeam.getTeamName().equals(teamName)) return bingoTeam;
        }
        return null;
    }

    public List<BingoTeam> getTeams() {
        return teams;
    }

    public boolean registerTeam(BingoTeam bingoTeam) {
        if (getTeam(bingoTeam.getTeamName()) != null) return false;
        this.teams.add(bingoTeam);
        return true;
    }

    public BingoTeam createNewTeam(BingoPlayer owner) {
        BingoTeam bingoTeam = new BingoTeam();
        bingoTeam.setOwner(owner);
        bingoTeam.setColor(BingoTeam.randomColor());
        bingoTeam.setPrefix(BingoTeam.randomPrefix());
        bingoTeam.setTeamName(this.plugin.getConfig().getString("default-team-name").replace("%number%", String.valueOf(this.teams.size())));
        return bingoTeam;
    }

    public boolean removeTeam(BingoTeam bingoTeam) {
        bingoTeam.getMembers().clear();
        return this.teams.remove(bingoTeam);
    }

    public BingoPlayer getPlayer(Player player) {
        BingoPlayer bingoPlayer = new BingoPlayer(player);
        for (BingoTeam bingoTeam : this.teams) {
            for (BingoPlayer bp : bingoTeam.getMembers()) {
                if (bingoPlayer.equals(bp)) return bp;
            }
        }
        return null;
    }

}
