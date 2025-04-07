package net.espectralgames.bingoEspectral.commands;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.ui.BingoCardMenu;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class bingocardCommand implements TabExecutor {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        YamlConfiguration lang = this.plugin.getLangConfig();
        if (commandSender instanceof Player player) {
            BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
            if (bingoPlayer != null) {
                if (bingoPlayer.getPersonalCard() != null) {
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                    new BingoCardMenu(bingoPlayer.getPersonalCard()).open(player);
                } else {
                    player.sendMessage(TextBuilder.error(lang.getString("bingo.error.cant_open_bingo_card")));
                }
            } else {
                player.sendMessage(TextBuilder.error(lang.getString("bingo.error.cant_open_bingo_card")));
            }
        } else {
            commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.must_be_player")));
        }
        return true;
    }
}
