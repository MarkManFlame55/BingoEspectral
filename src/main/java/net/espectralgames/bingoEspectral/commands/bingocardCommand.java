package net.espectralgames.bingoEspectral.commands;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.ui.BingoCardMenu;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
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
        final LangConfig lang = this.plugin.getLangConfig();
        if (commandSender instanceof Player player) {
            final BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
            if (bingoPlayer != null) {
                if (bingoPlayer.getPersonalCard() != null) {
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                    new BingoCardMenu(bingoPlayer.getPersonalCard(), bingoPlayer).open(player);
                } else {
                    player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.CANT_OPEN_BINGO_CARD)));
                }
            } else {
                player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.CANT_OPEN_BINGO_CARD)));
            }
        } else {
            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.MUST_BE_PLAYER)));
        }
        return true;
    }
}
