package net.espectralgames.bingoEspectral.commands;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.bingo.team.TeamInvite;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class bingoteamCommand implements TabExecutor {


    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame game = this.plugin.getBingoGame();

    private final Map<UUID, TeamInvite> pendingInvites = new HashMap<>();

    /*
        /bingoteam create [team_name] [color] [prefix]
        /bingoteam invite <Player> (owner exclusive)
        /bingoteam modify <team_name|color|prefix> <string_value> (owner exclusive)
        /bingoteam remove <Player> (owner exclusive)
        /bingoteam togglechat
        /bingoteam accept
        /bingoteam leave
     */


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (commandSender instanceof Player player) {
            final LangConfig lang = this.plugin.getLangConfig();
            if (args.length < 1) {
                commandSender.sendMessage(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS));
                return true;
            }

            if (args.length == 1) {
                switch (args[0]) {
                    case "create" -> {
                        BingoPlayer bingoPlayer = new BingoPlayer(player);
                        this.game.getTeamManager().createNewTeam(bingoPlayer);
                        player.sendMessage(TextBuilder.success(lang.team("team_created")));
                    }
                    case "togglechat" -> {
                        // ya vere como meto esto xd
                    }
                    case "aceept" -> {
                        TeamInvite invitacion = pendingInvites.get(player.getUniqueId());

                        if (invitacion == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.NO_PENDING_INVITES)));
                            return true;
                        }

                        Player teamOwner = Bukkit.getPlayer(invitacion.getOwner());
                        BingoTeam bingoTeam = this.game.getPlayer(teamOwner).getTeam();
                        if (bingoTeam == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.TEAM_DOESNT_EXIST)));
                        }

                        if (this.game.getTeamManager().getPlayer(player) != null) {
                            BingoPlayer bPlayer = this.game.getTeamManager().getPlayer(player);
                            if (!bPlayer.getTeam().removeMember(bPlayer)) {
                                System.out.println("Player no in that team");
                            }
                        }

                        assert bingoTeam != null;
                        if (bingoTeam.addMember(player)) {
                            bingoTeam.sendMessage(TextBuilder.info(lang.team("player_joined").replace("%player%", player.getName())).color(bingoTeam.getColor()));
                        } else {
                            player.sendMessage(TextBuilder.error(ErrorMessage.ALREDY_ON_THAT_TEAM));
                        }
                        invitacion.getTask().cancel();
                    }
                    case "leave" -> {
                        BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer != null) {
                            BingoTeam bingoTeam = bingoPlayer.getTeam();
                            bingoTeam.sendMessage(TextBuilder.info(lang.team("player_leave")));
                            bingoTeam.removeMember(bingoPlayer);
                        }
                    }
                }
                return true;
            }

            if (args.length == 2) {
                switch (args[0]) {
                    case "create" -> {
                        String teamName = args[1];
                        BingoPlayer bingoPlayer = new BingoPlayer(player);
                        BingoTeam bingoTeam = new BingoTeam(bingoPlayer, teamName, BingoTeam.randomPrefix(), BingoTeam.randomColor());
                        this.game.getTeamManager().registerTeam(bingoTeam);
                        player.sendMessage(TextBuilder.success(lang.team("team_created")));
                    }
                    case "invite" -> {
                        BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer != null && bingoPlayer.getTeam() != null) {
                            String playerName = args[1];
                            final Player invitedPlayer;
                            if ((invitedPlayer = Bukkit.getPlayer(playerName)) != null) {
                                if (pendingInvites.containsKey(invitedPlayer.getUniqueId())) {
                                    pendingInvites.get(invitedPlayer.getUniqueId()).getTask().cancel();
                                }

                                BukkitTask task = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        pendingInvites.remove(invitedPlayer.getUniqueId());
                                        invitedPlayer.sendMessage(TextBuilder.info(lang.team("invitation_expired").replace("%player%", player.getName())));
                                    }
                                }.runTaskLater(this.plugin, 20L  * 60);

                                pendingInvites.put(invitedPlayer.getUniqueId(), new TeamInvite(player.getUniqueId(), task));

                                Component message = TextBuilder.info(lang.team("received_invitation").replace("%player%", invitedPlayer.getName()))
                                        .append(TextBuilder.minimessage(lang.team("accept_invitation_button"))
                                                .clickEvent(ClickEvent.runCommand("/bingoespectral:bingoteam accept")));

                                invitedPlayer.sendMessage(message);
                                player.sendMessage(TextBuilder.success(lang.team("invitation_sent").replace("%player%", invitedPlayer.getName())));
                            }
                        } else {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                        }
                    }
                }
            }
            if (args.length == 3) {
                switch (args[0]) {
                    case "create" -> {
                        String teamName = args[1];
                        TextColor color = TextColor.fromCSSHexString(args[2]);
                        if (color == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.MALFORMED_COLOR_STRING)));
                            return true;
                        }

                        BingoPlayer bingoPlayer = new BingoPlayer(player);
                        BingoTeam bingoTeam = new BingoTeam(bingoPlayer, teamName, BingoTeam.randomPrefix(), color);
                        this.game.getTeamManager().registerTeam(bingoTeam);
                        player.sendMessage(TextBuilder.success(lang.team("team_created")));
                    }
                    case "modify" -> {
                        BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                        }
                        BingoTeam bingoTeam = bingoPlayer.getTeam();
                        if (bingoTeam == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                        }
                        assert bingoTeam != null;
                        if (bingoTeam.isOwner(bingoPlayer))  {
                            switch (args[1]) {
                                case "team_name" -> {
                                    String newTeamName = args[2];
                                    bingoTeam.setTeamName(newTeamName);
                                }
                                case "color" -> {
                                    TextColor color = TextColor.fromCSSHexString(args[2]);
                                    if (color == null) {
                                        player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.MALFORMED_COLOR_STRING)));
                                        return true;
                                    }
                                    bingoTeam.setColor(color);
                                }
                                case "prefix" -> {
                                    String newPrefix = args[2];
                                    bingoTeam.setPrefix(newPrefix);
                                }
                            }
                        }
                    }
                }
            }
            if (args.length == 4) {
                if (args[0].equals("create")) {

                    if (this.game.getTeamManager().getPlayer(player) != null) {
                        player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ALREDY_ON_THAT_TEAM)));
                    }

                    BingoPlayer bingoPlayer = new BingoPlayer(player);

                    String teamName = args[1];
                    TextColor color = TextColor.fromCSSHexString(args[2]);
                    String prefix = args[3];

                    if (color == null) {
                        player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.MALFORMED_COLOR_STRING)));
                        return true;
                    }

                    BingoTeam bingoTeam = new BingoTeam(bingoPlayer, teamName, prefix, color);

                    player.sendMessage(TextBuilder.success(lang.team("team_created")));

                }
            }
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
