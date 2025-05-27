package net.espectralgames.bingoEspectral.bingo.team;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BingoTeam {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();

    private final int MAX_PREFIX_LENGTH = 12;
    private BingoPlayer owner;
    private final List<BingoPlayer> members;
    private String prefix;
    private TextColor color;
    private String teamName;

    public BingoTeam(BingoPlayer owner, String teamName, String prefix, TextColor color) {
        this.teamName = teamName;
        this.setPrefix(prefix);
        this.color = color;
        this.members = new ArrayList<>();
        this.setOwner(owner);
    }
    public BingoTeam() {
        this.members = new ArrayList<>();
    }


    public void sendMessage(Component message) {
        for (BingoPlayer bingoPlayer : this.members) {
            bingoPlayer.sendMessage(message);
        }
    }

    public boolean addMember(Player player) {
        final BingoPlayer bingoPlayer = new BingoPlayer(player);
        return addMember(bingoPlayer);
    }

    public boolean addMember(BingoPlayer bingoPlayer) {
        if (this.members.contains(bingoPlayer)) return false;
        this.members.add(bingoPlayer);
        bingoPlayer.setTeam(this);
        return true;
    }

    public boolean removeMember(BingoPlayer bingoPlayer) {
        if (this.members.remove(bingoPlayer)) {
            if (isOwner(bingoPlayer)) setRandomOwner();
            bingoPlayer.setTeam(null);
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
        if (prefix.length() <= this.MAX_PREFIX_LENGTH) {
            this.prefix = prefix;
        } else {
            this.prefix = prefix.substring(0, MAX_PREFIX_LENGTH);
        }

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
        final LangConfig lang = this.plugin.getLangConfig();
        this.owner = owner;
        addMember(owner);
        owner.getPlayer().sendMessage(TextBuilder.info(lang.team("new_team_owner")));
    }

    public boolean isOwner(BingoPlayer bingoPlayer) {
        return bingoPlayer.equals(this.owner);
    }

    public void setRandomOwner() {
        if (!this.members.isEmpty()) {
            BingoPlayer newOwner = this.members.get(new Random().nextInt(this.members.size()));
            setOwner(newOwner);
        }
    }

    public int getTeamPoints() {
        int points = 0;
        for (BingoPlayer bingoPlayer : this.members) {
            points += bingoPlayer.getPoints();
        }
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BingoTeam bingoTeam)) return false;
        return Objects.equals(plugin, bingoTeam.plugin) && Objects.equals(bingoGame, bingoTeam.bingoGame) && Objects.equals(owner, bingoTeam.owner) && Objects.equals(members, bingoTeam.members) && Objects.equals(prefix, bingoTeam.prefix) && Objects.equals(color, bingoTeam.color) && Objects.equals(teamName, bingoTeam.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, bingoGame, MAX_PREFIX_LENGTH, owner, members, prefix, color, teamName);
    }

    public static TextColor randomColor() {
        List<TextColor> colors = List.of(
                NamedTextColor.BLACK, NamedTextColor.DARK_BLUE, NamedTextColor.DARK_GREEN, NamedTextColor.DARK_AQUA,
                NamedTextColor.DARK_RED, NamedTextColor.DARK_PURPLE, NamedTextColor.GOLD, NamedTextColor.GRAY,
                NamedTextColor.DARK_GRAY, NamedTextColor.BLUE, NamedTextColor.GREEN, NamedTextColor.AQUA,
                NamedTextColor.RED, NamedTextColor.LIGHT_PURPLE, NamedTextColor.YELLOW, NamedTextColor.WHITE);
        return colors.get(new Random().nextInt(colors.size()));
    }
    public static String randomPrefix() {
        List<Character> chars = List.of(
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '0','1','2','3','4','5','6','7','8','9');
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char c = chars.get(new Random().nextInt(chars.size()));
            builder.append(c);
        }
        return builder.toString();
    }
}
