package net.espectralgames.bingoEspectral.commands;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class bingoteamCommand implements TabExecutor {


    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame game = this.plugin.getBingoGame();

    /*
        /bingoteam create [team_name] [color] [prefix]
        /bingoteam invite <Player> (owner exclusive)
        /bingoteam modify <team_name|color|prefix> <string_value> (owner exclusive)
        /bingoteam remove <Player> (owner exclusive)
        /bingoteam togglechat

     */


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        final LangConfig lang = this.plugin.getLangConfig();
        if (args.length < 1) {
            commandSender.sendMessage(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS));
            return true;
        }

        if (args.length == 1) {
            switch (args[0]) {
                case "create" -> {

                }
                case "togglechat" -> {
                    // ya vere como meto esto xd
                }
            }
            return true;
        }

        if (args.length == 2) {
            switch (args[0]) {
                case "create" -> {
                    String teamName = args[1];
                    // Crear equipo con ese nombre
                }
                case "invite" -> {
                    String playerName = args[1];
                    final Player player;
                    if ((player = Bukkit.getPlayer(playerName)) != null) {

                    }
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
