package net.espectralgames.bingoEspectral.bingo.team;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
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

    public void registerTeam(BingoTeam bingoTeam) {
        if (this.teams.contains(bingoTeam)) return;
        this.teams.add(bingoTeam);
    }

    public void createNewTeam(BingoPlayer owner) {
        BingoTeam bingoTeam = new BingoTeam();
        bingoTeam.setOwner(owner);
        bingoTeam.setColor(BingoTeam.randomColor());
        bingoTeam.setPrefix(BingoTeam.randomPrefix());
        int teamNumber = this.teams.size() - 1;
        bingoTeam.setTeamName(this.plugin.getConfig().getString("default-team-name").replace("%number%", String.valueOf(teamNumber)));
        bingoTeam.addMember(owner);
        registerTeam(bingoTeam);
    }

    public void removeTeam(BingoTeam bingoTeam) {
        bingoTeam.getMembers().clear();
        this.teams.remove(bingoTeam);
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

    public void clearTeams() {
        for (BingoTeam bingoTeam : new ArrayList<>(this.teams)) {
            removeTeam(bingoTeam);
        }
    }

    public List<BingoTeam> getTopPoints() {
        List<BingoTeam> orderTeams = new ArrayList<>(this.teams);
        orderTeams.sort(Comparator.comparingInt(BingoTeam::getTeamPoints).reversed());
        return orderTeams;
    }
}
