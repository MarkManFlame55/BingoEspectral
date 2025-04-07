package net.espectralgames.bingoEspectral.commands;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.ui.BingoCardMenu;
import net.espectralgames.bingoEspectral.ui.BingoOptionsMenu;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class bingoCommand implements TabExecutor {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        final YamlConfiguration lang = this.plugin.getLangConfig();
        if (args.length < 1) {
            commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.bad_command_arguments")));
        } else {

            if (args.length == 1) {
                switch (args[0]) {
                    case "card" -> {
                        if (commandSender instanceof Player player) {
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                            if (this.bingoGame.getBingoCard() != null) {
                                new BingoCardMenu(this.bingoGame.getBingoCard()).open(player);
                            } else {
                                commandSender.sendMessage(TextBuilder.minimessage(lang.getString("bingo.error.cant_open_bingo_card")));
                            }

                        }
                    }
                    case "options" -> {
                        if (commandSender instanceof Player sender) {
                            new BingoOptionsMenu().open(sender);
                        } else {
                            commandSender.sendMessage(TextBuilder.minimessage(lang.getString("bingo.error.must_be_player")));
                        }
                    }
                    case "stop" -> {
                        this.bingoGame.finishGame();
                    }
                    case "help" -> {
                        List<Component> msg = new ArrayList<>();
                        msg.add(TextBuilder.minimessage("<dark_gray>================"));
                        msg.add(TextBuilder.minimessage(lang.getString("bingo.prefix")));
                        msg.add(Component.text(""));
                        msg.add(commandHelp("/bingo generate <seed>", lang.getString("bingo.help.generate")));
                        msg.add(commandHelp("/bingo card", lang.getString("bingo.help.card")));
                        msg.add(commandHelp("/bingo card <Player>", lang.getString("bingo.help.card_of_player")));
                        msg.add(commandHelp("/bingo options", lang.getString("bingo.help.options")));
                        msg.add(commandHelp("/bingo start", lang.getString("bingo.help.start")));
                        msg.add(commandHelp("/bingo stop", lang.getString("bingo.help.stop")));
                        msg.add(commandHelp("/bingo reload", lang.getString("bingo.help.reload")));
                        msg.add(commandHelp("/bingocard", lang.getString("bingo.help.bingocard")));
                        msg.add(commandHelp("/bingo help", lang.getString("bingo.help.help")));
                        msg.add(TextBuilder.minimessage("<dark_gray>================"));
                        msg.forEach(commandSender::sendMessage);
                        if (commandSender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                    }
                    case "start" -> {
                        if (this.bingoGame.getBingoCard().isGenerated()) {
                            this.bingoGame.startGame();
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.cant_start_without_card")));
                            if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                        }

                    }
                    case "reload" -> {
                        this.plugin.reloadConfig();
                        this.plugin.reloadCardsConfig();
                        this.plugin.reloadLangConfig();
                        commandSender.sendMessage(TextBuilder.success(lang.getString("bingo.config.reload")));
                        if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                    }
                    case "generate" -> {
                        String seed = this.plugin.getConfig().getString("last-bingo-seed-played");
                        if (this.bingoGame.getBingoCard().generate(seed) != null) {
                            commandSender.sendMessage(TextBuilder.success(lang.getString("bingo.game.card_generated")));
                            commandSender.sendMessage(TextBuilder.info("<aqua>Seed: <white>" + seed));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.failed_card_generation")));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                        }
                    }
                    default -> {
                        commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.bad_command_arguments")));
                        if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                    }
                }
            }
            if (args.length == 2) {

                switch(args[0]) {
                    case "generate" -> {
                        String seed = args[1];
                        if (this.bingoGame.getBingoCard().generate(seed) != null) {
                            commandSender.sendMessage(TextBuilder.success(lang.getString("bingo.game.card_generated")));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.failed_card_generation")));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                        }
                    }
                    case "card" -> {
                        if (commandSender instanceof Player sender) {
                            Player player = Bukkit.getPlayer(args[1]);
                            if (player != null) {
                                BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
                                if (bingoPlayer != null) {
                                    new BingoCardMenu(bingoPlayer.getPersonalCard()).open(sender);
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                                } else {
                                    commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.player_not_in_game")));
                                    sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                                }
                            } else {
                                commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.player_does_not_exist")));
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                            }
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.must_be_player")));
                        }
                    }
                    default -> {
                        commandSender.sendMessage(TextBuilder.error(lang.getString("bingo.error.bad_command_arguments")));
                        if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1 && commandSender instanceof Player) {
            return List.of("generate", "start", "card", "options", "help", "stop", "reload");
        }
        if (args.length == 2 && commandSender instanceof Player) {
            if (args[0].equals("card")) return null;
            if (args[0].equals("generate")) return List.of("<seed>");
        }
        return List.of();
    }

    private Component commandHelp(String command, String description) {
        return TextBuilder.minimessage("<gray>- <b><gold>" + command + "</b><white>: " + description);
    }
}
