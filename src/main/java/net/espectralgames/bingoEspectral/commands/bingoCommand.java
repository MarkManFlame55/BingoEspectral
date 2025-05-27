package net.espectralgames.bingoEspectral.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.item.BingoCardItem;
import net.espectralgames.bingoEspectral.ui.BingoCardMenu;
import net.espectralgames.bingoEspectral.ui.BingoOptionsMenu;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class bingoCommand implements TabExecutor {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();
    private final Server server = Bukkit.getServer();

    // bingo points add/get/set <Player> <int>

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        final LangConfig lang = this.plugin.getLangConfig();
        if (args.length < 1) {
            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS)));
        } else {

            if (args.length == 1) {
                switch (args[0]) {
                    case "card" -> {
                        if (commandSender instanceof Player player) {
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                            if (this.bingoGame.getBingoCard() != null) {
                                new BingoCardMenu(this.bingoGame.getBingoCard()).open(player);
                            } else {
                                commandSender.sendMessage(TextBuilder.minimessage(lang.error(ErrorMessage.CANT_OPEN_BINGO_CARD)));
                            }
                        }
                    }
                    case "options" -> {
                        if (commandSender instanceof Player sender) {
                            new BingoOptionsMenu().open(sender);
                        } else {
                            commandSender.sendMessage(TextBuilder.minimessage(lang.error(ErrorMessage.MUST_BE_PLAYER)));
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
                        msg.add(commandHelp("/bingo generate <seed>", lang.help("generate")));
                        msg.add(commandHelp("/bingo card", lang.help("card")));
                        msg.add(commandHelp("/bingo card <Player>", lang.help("card_of_player")));
                        msg.add(commandHelp("/bingo options", lang.help("options")));
                        msg.add(commandHelp("/bingo start", lang.help("start")));
                        msg.add(commandHelp("/bingo stop", lang.help("stop")));
                        msg.add(commandHelp("/bingo reload", lang.help("reload")));
                        msg.add(commandHelp("/bingo ls", lang.help("ls")));
                        msg.add(commandHelp("/bingo clearteams", lang.help("clearteams")));
                        msg.add(commandHelp("/bingo points add <Player> <points>", lang.help("points.add")));
                        msg.add(commandHelp("/bingo points set <Player> <points>", lang.help("points.set")));
                        msg.add(commandHelp("/bingo points get <Player>", lang.help("points.get")));
                        msg.add(commandHelp("/bingo points top", lang.help("points.top")));
                        msg.add(commandHelp("/bingocard", lang.help("bingocard")));
                        msg.add(commandHelp("/bingo help", lang.help("help")));
                        msg.add(TextBuilder.minimessage("<dark_gray>================"));
                        msg.forEach(commandSender::sendMessage);
                        if (commandSender instanceof Player player) player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                    }
                    case "start" -> {
                        if (this.bingoGame.getBingoCard().isGenerated()) {
                            this.bingoGame.startGame();
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.CANT_START_WITHOUT_CARD)));
                            if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                        }

                    }
                    case "reload" -> {
                        this.plugin.reloadConfig();
                        this.plugin.reloadCardsConfig();
                        this.plugin.reloadLangConfig();
                        commandSender.sendMessage(TextBuilder.success(lang.config("reload")));
                        if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                    }
                    case "generate" -> {
                        final String seed = this.plugin.getConfig().getString("last-bingo-seed-played");
                        if (this.bingoGame.getBingoCard().generate(seed) != null) {
                            commandSender.sendMessage(TextBuilder.success(lang.game("card_generated")));
                            commandSender.sendMessage(TextBuilder.info("<aqua>Seed: <white>" + seed));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.FAILED_CARD_GENERATION)));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                        }
                    }
                    case "clearteams" -> {
                        this.bingoGame.getTeamManager().clearTeams();
                    }
                    default -> {
                        commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS)));
                        if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                    }
                }
            }
            if (args.length == 2) {

                switch(args[0]) {
                    case "generate" -> {
                        String seed = args[1];
                        if (this.bingoGame.getBingoCard().generate(seed) != null) {
                            commandSender.sendMessage(TextBuilder.success(lang.game("card_generated")));
                            if (commandSender instanceof Player sender)
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.FAILED_CARD_GENERATION)));
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
                                    if (bingoPlayer.getPersonalCard() != null) {
                                        new BingoCardMenu(bingoPlayer.getPersonalCard(), bingoPlayer).open(sender);
                                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 2.0f);
                                    } else {
                                        commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.CANT_OPEN_BINGO_CARD)));
                                        sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                                    }
                                } else {
                                    commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_GAME)));
                                    sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                                }
                            } else {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                            }
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.MUST_BE_PLAYER)));
                        }
                    }
                    case "ls" -> {
                        if (this.bingoGame.isStarted()) {
                            Player player = Bukkit.getPlayer(args[1]);

                            if (player == null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                                return true;
                            }

                            if (this.bingoGame.getPlayer(player) != null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_ALREADY_IN_GAME)));
                            }

                            BingoPlayer bingoPlayer = new BingoPlayer(player);
                            Random random = new Random();
                            this.bingoGame.addPlayer(bingoPlayer);
                            this.bingoGame.getTeamManager().createNewTeam(bingoPlayer);

                            double x = random.nextDouble(-this.bingoGame.getOptions().getSpreadDistance(), this.bingoGame.getOptions().getSpreadDistance());
                            double z = random.nextDouble(-this.bingoGame.getOptions().getSpreadDistance(), this.bingoGame.getOptions().getSpreadDistance());

                            Location location = new Location(player.getWorld(),
                                    x,
                                    player.getWorld().getHighestBlockYAt((int) x, (int) z, HeightMap.MOTION_BLOCKING) + 1,
                                    z);

                            // TP, Respawn Point y /clear
                            player.teleport(location);

                            // Unlock recipes
                            server.recipeIterator().forEachRemaining(recipe -> {
                                if (recipe instanceof Keyed keyedRecipe)  {
                                    player.discoverRecipe(keyedRecipe.getKey());
                                }
                            });

                            Block spawnBlock = player.getWorld().getBlockAt(location.clone().add(0,-1, 0));
                            if (spawnBlock.isLiquid()) {
                                player.getWorld().setBlockData(spawnBlock.getLocation(), Bukkit.createBlockData(Material.BEDROCK));
                            }

                            player.setRespawnLocation(location, true);
                            player.getInventory().clear();

                            // Efectos para que empiecen bien
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 255, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 10, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 255, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 255, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 255, false, false, false));

                            player.setGameMode(GameMode.SURVIVAL);
                            bingoPlayer.setPersonalCard(this.bingoGame.getBingoCard().copy());
                            player.give(new ItemStack(Material.OAK_BOAT), BingoCardItem.item());
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_HASNT_STARTED)));
                        }
                    }
                    case "points" -> {
                        if (args[1].equals("top")) {
                            commandSender.sendMessage(TextBuilder.minimessage(lang.getString("bingo.prefix")));
                            List<BingoTeam> topTeams = this.bingoGame.getTeamManager().getTopPoints();
                            for (int i = 0; i < topTeams.size(); i++) {
                                BingoTeam bingoTeam = topTeams.get(i);

                                Component numberComponent;

                                switch (i) {
                                    case 0 -> {
                                        numberComponent = TextBuilder.minimessage("<b><gold>[" + (i + 1) + "º]</b> ");
                                    }
                                    case 1 -> {
                                        numberComponent = TextBuilder.minimessage("<b><gray>[" + (i + 1) + "º]</b> ");
                                    }
                                    case 2 -> {
                                        numberComponent = TextBuilder.minimessage("<b><#CD7F32>[" + (i + 1) + "º]</b>");
                                    }
                                    default -> numberComponent = TextBuilder.minimessage("<b><white>[" + (i + 1) + "º]</b> ");
                                }

                                commandSender.sendMessage(numberComponent
                                        .append(TextBuilder.minimessage(bingoTeam.getTeamName()).color(bingoTeam.getColor()))
                                        .append(TextBuilder.minimessage("<white> - <aqua>" + bingoTeam.getTeamPoints())));
                            }

                        }
                    }
                    default -> {
                        commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS)));
                        if (commandSender instanceof Player sender) sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                    }
                }
            }
            if (args.length == 3) {
                switch (args[0]) {
                    case "ls" -> {
                        if (this.bingoGame.isStarted()) {
                            Player player = Bukkit.getPlayer(args[1]);
                            if (player == null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                                return true;
                            }
                            String teamName = args[2];
                            BingoTeam bingoTeam = this.bingoGame.getTeamManager().getTeam(teamName);
                            if (bingoTeam == null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.TEAM_DOESNT_EXIST)));
                                return true;
                            }
                            if (this.bingoGame.getPlayer(player) != null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_ALREADY_IN_GAME)));
                                return true;
                            }
                            BingoPlayer bingoPlayer = new BingoPlayer(player);
                            this.bingoGame.addPlayer(bingoPlayer);

                            Location teamSpawn = bingoTeam.getMembers().getFirst().getPlayer().getRespawnLocation();
                            bingoTeam.addMember(bingoPlayer);
                            // Unlock recipes
                            server.recipeIterator().forEachRemaining(recipe -> {
                                if (recipe instanceof Keyed keyedRecipe)  {
                                    player.discoverRecipe(keyedRecipe.getKey());
                                }
                            });

                            Block spawnBlock = player.getWorld().getBlockAt(teamSpawn.clone().add(0,-1, 0));
                            if (spawnBlock.isLiquid()) {
                                player.getWorld().setBlockData(spawnBlock.getLocation(), Bukkit.createBlockData(Material.BEDROCK));
                            }

                            player.setRespawnLocation(teamSpawn, true);
                            player.getInventory().clear();
                            player.teleport(teamSpawn);

                            // Efectos para que empiecen bien
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 255, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 10, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 255, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 255, false, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 255, false, false, false));

                            player.setGameMode(GameMode.SURVIVAL);
                            bingoPlayer.setPersonalCard(this.bingoGame.getBingoCard().copy());
                            player.give(new ItemStack(Material.OAK_BOAT), BingoCardItem.item());
                        } else {
                            commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_HASNT_STARTED)));
                        }
                    }
                    case "points" -> {
                        String option = args[1];
                        if (option.equals("get")) {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player == null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                                return true;
                            }

                            if (this.bingoGame.isStarted()) {
                                final BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
                                if (bingoPlayer == null) {
                                    commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_GAME)));
                                    return true;
                                }
                                final BingoTeam bingoTeam = bingoPlayer.getTeam();
                                int points = bingoTeam.getTeamPoints();
                                commandSender.sendMessage("El equipo de " + player.getName() + " tiene " + points + " puntos.");
                            }
                        }
                    }
                }
            }
            if (args.length == 4) {
                if (args[0].equals("points")) {
                    String option = args[1];
                    switch (option) {
                        case "add" -> {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player == null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                                return true;
                            }
                            if (!isNumber(args[3])) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS)));
                                return true;
                            }
                            int points = Integer.parseInt(args[3]);

                            if (this.bingoGame.isStarted()) {
                                final BingoPlayer bingoPlayer =  this.bingoGame.getPlayer(player);
                                if (bingoPlayer == null) {
                                    commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_GAME)));
                                    return true;
                                }
                                bingoPlayer.addPoints(points);
                            } else {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_HASNT_STARTED)));
                                return true;
                            }
                        }
                        case "set" -> {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player == null) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_DOES_NOT_EXIST)));
                                return true;
                            }
                            if (!isNumber(args[3])) {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.BAD_COMMAND_ARGUMENTS)));
                                return true;
                            }
                            int points = Integer.parseInt(args[3]);

                            if (this.bingoGame.isStarted()) {
                                final BingoPlayer bingoPlayer =  this.bingoGame.getPlayer(player);
                                if (bingoPlayer == null) {
                                    commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.PLAYER_NOT_IN_GAME)));
                                    return true;
                                }
                                bingoPlayer.setPoints(points);
                            } else {
                                commandSender.sendMessage(TextBuilder.error(lang.error(ErrorMessage.GAME_HASNT_STARTED)));
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return List.of("generate", "start", "card", "options", "help", "stop", "reload", "ls", "clearteams", "points");
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "card" -> {
                    return List.of("");
                }
                case "generate" -> {
                    return List.of("<seed>");
                }
                case "ls" -> {
                    return null;
                }
                case "points" -> {
                    return List.of("get", "set", "add", "top");
                }
            }
        }
        if (args.length == 3) {
            if (args[0].equals("ls")) {
                List<String> teamList = new ArrayList<>();
                for (BingoTeam bingoTeam : this.bingoGame.getTeamManager().getTeams()) {
                    teamList.add(bingoTeam.getTeamName());
                }
                return teamList;
            }
            if (args[0].equals("points")) {
                return null;
            }
        }
        if (args.length == 4) {
            if (args[0].equals("points")) {
                if (args[1].equals("get") || args[1].equals("set")) {
                    return List.of("<points>");
                }
            }
        }
        return List.of();
    }

    private Component commandHelp(String command, String description) {
        return TextBuilder.minimessage("<gray>- <b><gold>" + command + "</b><white>: " + description);
    }

    private boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
