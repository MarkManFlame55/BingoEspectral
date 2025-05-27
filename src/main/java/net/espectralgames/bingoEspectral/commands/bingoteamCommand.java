package net.espectralgames.bingoEspectral.commands;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.bingo.team.TeamChatManager;
import net.espectralgames.bingoEspectral.bingo.team.TeamInvite;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
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

import java.util.*;

public class bingoteamCommand implements TabExecutor {


    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame game = this.plugin.getBingoGame();

    private final Map<UUID, TeamInvite> pendingInvites = new HashMap<>();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return List.of("create", "invite", "modify", "remove", "togglechat", "accept", "leave", "list", "help");
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "create" -> {
                    return List.of("[team_name]");
                }
                case "invite", "remove" -> {
                    return null;
                }
                case "modify" -> {
                    return List.of("team_name", "color", "prefix");
                }
                default -> {
                    return List.of();
                }
            }
        }
        if (args.length == 3) {
            switch (args[0]) {
                case "create" -> {
                    return List.of("[color]");
                }
                case "modify" -> {
                    if (args[1].equals("color")) {
                        return List.of("aqua", "black", "blue", "dark_aqua", "dark_blue", "dark_gray"," dark_green", "dark_purple", "dark_red", "gold", "gray", "green", "light_purple", "red", "white", "yellow", "#<hex_value>");
                    }
                }
                default -> {
                    return List.of();
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equals("create")) {
                return List.of("[prefix]");
            } else {
                return List.of();
            }
        }
        return List.of();
    }

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
                        if (this.game.getOptions().isRandomTeams()) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PREVENTED_SELF_ACTION)));
                            return true;
                        }
                        if (this.game.getTeamManager().getPlayer(player) != null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ALREDY_ON_THAT_TEAM)));
                        }
                        if (!this.game.isStarted()) {
                            BingoPlayer bingoPlayer = new BingoPlayer(player);
                            this.game.getTeamManager().createNewTeam(bingoPlayer);
                            player.sendMessage(TextBuilder.success(lang.team("team_created")));
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_ALREADY_STARTED)));
                        }
                    }
                    case "list" -> {
                        BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.TEAM_DOESNT_EXIST)));
                            return true;
                        }
                        BingoTeam bingoTeam = bingoPlayer.getTeam();
                        if (bingoTeam == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                            return true;
                        }
                        player.sendMessage(TextBuilder.minimessage("[" + bingoTeam.getTeamName() + "] <white>:").color(bingoTeam.getColor()));
                        for (BingoPlayer bp : bingoTeam.getMembers()) {
                            player.sendMessage(TextBuilder.minimessage("<gray>- <white>" + bp.getName()));
                        }
                    }
                    case "togglechat" -> {
                        TeamChatManager.getTeamChatMap().putIfAbsent(player, false);
                        TeamChatManager.getTeamChatMap().put(player, !TeamChatManager.getTeamChatMap().get(player));
                        if (TeamChatManager.getTeamChatMap().get(player)) {
                            player.sendMessage(TextBuilder.info(lang.team("team_chat_set")));
                        } else {
                            player.sendMessage(TextBuilder.info(lang.team("global_chat_set")));
                        }
                    }
                    case "accept" -> {
                        if (this.game.isStarted()) {
                            player.sendMessage(TextBuilder.error(ErrorMessage.GAME_ALREADY_STARTED));
                            return true;
                        }
                        TeamInvite invitacion = pendingInvites.get(player.getUniqueId());

                        if (invitacion == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.NO_PENDING_INVITES)));
                            return true;
                        }

                        Player teamOwner = Bukkit.getPlayer(invitacion.getOwner());
                        BingoTeam bingoTeam = this.game.getTeamManager().getPlayer(teamOwner).getTeam();
                        if (bingoTeam == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.TEAM_DOESNT_EXIST)));
                            return true;
                        }

                        if (this.game.getTeamManager().getPlayer(player) != null) {
                            BingoPlayer bPlayer = this.game.getTeamManager().getPlayer(player);
                            if (!bPlayer.getTeam().removeMember(bPlayer)) {
                                System.out.println("Player no in that team");
                            }
                        }

                        if (bingoTeam.addMember(player)) {
                            bingoTeam.sendMessage(TextBuilder.info(lang.team("player_join").replace("%player%", player.getName())).color(bingoTeam.getColor()));
                        } else {
                            player.sendMessage(TextBuilder.error(ErrorMessage.ALREDY_ON_THAT_TEAM));
                        }
                        invitacion.getTask().cancel();
                    }
                    case "leave" -> {
                        if (this.game.isStarted()) {
                            player.sendMessage(TextBuilder.error(ErrorMessage.GAME_ALREADY_STARTED));
                            return true;
                        }

                        BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer != null) {
                            BingoTeam bingoTeam = bingoPlayer.getTeam();
                            if (bingoTeam == null) {
                                player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                                return true;
                            }
                            bingoTeam.sendMessage(TextBuilder.info(lang.team("player_leave").replace("%player%", player.getName())));
                            bingoTeam.removeMember(bingoPlayer);
                            if (bingoTeam.getMembers().isEmpty()) {
                                this.game.getTeamManager().removeTeam(bingoTeam);
                            }
                        }
                    }
                    case "help" -> {
                        List<Component> msg = new ArrayList<>();
                        msg.add(TextBuilder.minimessage("<dark_gray>================"));
                        msg.add(TextBuilder.minimessage(lang.getString("bingo.prefix")));
                        msg.add(Component.text(""));
                        msg.add(commandHelp("/bingoteam accept", lang.help("bingoteam.accept")));
                        msg.add(commandHelp("/bingoteam create [team_name] [color] [prefix]", lang.help("bingoteam.create")));
                        msg.add(commandHelp("/bingoteam invite <Player>", lang.help("bingoteam.invite")));
                        msg.add(commandHelp("/bingoteam leave", lang.help("bingoteam.leave")));
                        msg.add(commandHelp("/bingoteam modify <team_name|color|prefix> <value>", lang.help("bingoteam.modify")));
                        msg.add(commandHelp("/bingoteam remove <Player>", lang.help("bingoteam.remove")));
                        msg.add(commandHelp("/bingoteam list", lang.help("bingoteam.list")));
                        msg.add(commandHelp("/bingoteam togglechat", lang.help("bingoteam.togglechat")));
                        msg.add(commandHelp("/bingoteam help", lang.help("bingoteam.help")));
                        msg.add(TextBuilder.minimessage("<dark_gray>================"));
                        msg.forEach(commandSender::sendMessage);
                    }
                }
                return true;
            }

            if (args.length == 2) {
                switch (args[0]) {
                    case "create" -> {
                        if (this.game.isStarted()) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_ALREADY_STARTED)));
                            return true;
                        }
                        if (this.game.getOptions().isRandomTeams()) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PREVENTED_SELF_ACTION)));
                            return true;
                        }
                        if (this.game.getTeamManager().getPlayer(player) != null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ALREDY_ON_THAT_TEAM)));
                        }
                        String teamName = args[1];
                        BingoPlayer bingoPlayer = new BingoPlayer(player);
                        BingoTeam bingoTeam = new BingoTeam(bingoPlayer, teamName, BingoTeam.randomPrefix(), BingoTeam.randomColor());
                        this.game.getTeamManager().registerTeam(bingoTeam);
                        player.sendMessage(TextBuilder.success(lang.team("team_created")));
                    }
                    case "invite" -> {
                        if (this.game.isStarted()) {
                            player.sendMessage(TextBuilder.error(ErrorMessage.GAME_ALREADY_STARTED));
                            return true;
                        }
                        BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer != null && bingoPlayer.getTeam() != null) {
                            final BingoTeam bingoTeam = bingoPlayer.getTeam();
                            if (bingoTeam.isOwner(bingoPlayer)) {
                                String playerName = args[1];
                                final Player invitedPlayer;
                                if ((invitedPlayer = Bukkit.getPlayer(playerName)) != null) {
                                    if (pendingInvites.containsKey(invitedPlayer.getUniqueId())) {
                                        pendingInvites.get(invitedPlayer.getUniqueId()).getTask().cancel();
                                    }

                                    if (player.getUniqueId().equals(invitedPlayer.getUniqueId())) {
                                        player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PREVENTED_SELF_ACTION)));
                                        return true;
                                    }

                                    BukkitTask task = new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            pendingInvites.remove(invitedPlayer.getUniqueId());
                                            invitedPlayer.sendMessage(TextBuilder.info(lang.team("invitation_expired").replace("%player%", player.getName())));
                                        }
                                    }.runTaskLater(this.plugin, 20L  * 60);

                                    pendingInvites.put(invitedPlayer.getUniqueId(), new TeamInvite(player.getUniqueId(), task));

                                    Component message = TextBuilder.info(lang.team("received_invitation").replace("%player%", player.getName()))
                                            .append(TextBuilder.minimessage(" " + lang.team("accept_invitation_button"))
                                                    .clickEvent(ClickEvent.runCommand("/bingoteam accept"))
                                                    .hoverEvent(HoverEvent.showText(TextBuilder.minimessage(lang.team("accept_invitation_button_hover")))));

                                    invitedPlayer.sendMessage(message);
                                    player.sendMessage(TextBuilder.success(lang.team("invitation_sent").replace("%player%", invitedPlayer.getName())));
                                }
                            } else {
                                player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ONLY_OWNER_ALLOWED)));
                            }
                        } else {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                        }
                    }
                    case "remove" -> {

                        if (this.game.isStarted()) {
                            player.sendMessage(TextBuilder.error(ErrorMessage.GAME_ALREADY_STARTED));
                            return true;
                        }

                        final BingoPlayer bingoPlayer = this.game.getTeamManager().getPlayer(player);
                        if (bingoPlayer == null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                            return true;
                        }
                        BingoTeam bingoTeam = bingoPlayer.getTeam();
                        if (bingoTeam.isOwner(bingoPlayer)) {
                            Player removedPlayer = Bukkit.getPlayer(args[1]);
                            if (removedPlayer != null) {

                                if (player.getUniqueId().equals(removedPlayer.getUniqueId())) {
                                    player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PREVENTED_SELF_ACTION)));
                                    return true;
                                }

                                BingoPlayer removedBingoPlayer = this.game.getTeamManager().getPlayer(removedPlayer);
                                if (removedBingoPlayer != null) {
                                    bingoTeam.sendMessage(TextBuilder.info(lang.team("player_leave").replace("%player%", removedPlayer.getName())));
                                    bingoTeam.removeMember(removedBingoPlayer);
                                } else {
                                    player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_A_TEAM)));
                                }
                            } else {
                                player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                            }
                        } else {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ONLY_OWNER_ALLOWED)));
                        }

                    }
                }
            }
            if (args.length == 3) {
                switch (args[0]) {
                    case "create" -> {

                        if (this.game.isStarted()) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_ALREADY_STARTED)));
                            return true;
                        }
                        if (this.game.getOptions().isRandomTeams()) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PREVENTED_SELF_ACTION)));
                            return true;
                        }
                        if (this.game.getTeamManager().getPlayer(player) != null) {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ALREDY_ON_THAT_TEAM)));
                        }
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
                                    player.sendMessage(TextBuilder.success(lang.team("team_renamed").replace("%name%", newTeamName)));
                                }
                                case "color" -> {
                                    String colorCode = args[2].trim();
                                    if (NamedTextColor.NAMES.value(colorCode) != null) {
                                        NamedTextColor color = NamedTextColor.NAMES.value(colorCode);
                                        bingoTeam.setColor(color);
                                    } else {
                                        TextColor color = TextColor.fromCSSHexString(colorCode);
                                        if (color == null) {
                                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.MALFORMED_COLOR_STRING)));
                                            return true;
                                        }
                                        bingoTeam.setColor(color);
                                    }
                                    player.sendMessage(TextBuilder.success(lang.team("team_recolored").replace("%color%", colorCode)));
                                }
                                case "prefix" -> {
                                    String newPrefix = args[2];
                                    bingoTeam.setPrefix(newPrefix);
                                    player.sendMessage(TextBuilder.success(lang.team("team_new_prefix").replace("%prefix%", bingoTeam.getPrefix())));
                                }
                            }
                        } else {
                            player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.ONLY_OWNER_ALLOWED)));
                        }
                    }
                }
            }
            if (args.length == 4) {
                if (args[0].equals("create")) {

                    if (this.game.isStarted()) {
                        player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_ALREADY_STARTED)));
                        return true;
                    }
                    if (this.game.getOptions().isRandomTeams()) {
                        player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PREVENTED_SELF_ACTION)));
                        return true;
                    }
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

                    this.game.getTeamManager().registerTeam(bingoTeam);
                }
            }
        }


        return true;
    }

    private Component commandHelp(String command, String description) {
        return TextBuilder.minimessage("<gray>- <b><gold>" + command + "</b><white>: " + description);
    }
}
