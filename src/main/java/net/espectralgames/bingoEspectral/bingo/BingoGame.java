package net.espectralgames.bingoEspectral.bingo;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.options.BingoOptions;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.bingo.team.TeamManager;
import net.espectralgames.bingoEspectral.item.BingoCardItem;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

public class BingoGame {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final Server server = Bukkit.getServer();
    private final List<BingoPlayer> players = new ArrayList<>();
    private final BingoOptions options = new BingoOptions();
    private final BingoCard bingoCard = new BingoCard();
    private final TeamManager teamManager = new TeamManager();
    private Team spectatorTeam;
    private boolean started;
    private boolean waitingToStart = true;

    /**
     * @return {@link BingoCard} used for the game
     */
    public BingoCard getBingoCard() {
        return bingoCard;
    }

    /**
     * @return List of players in the game.
     */
    public List<BingoPlayer> getPlayerList() {
        return players;
    }

    /**
     * Gets the {@link BingoPlayer} out of a {@link Player} if it`s playing the Game.
     *
     * @param player Player to get
     * @return {@link BingoPlayer} if the player is in the game, or {@code null} if not
     */
    public BingoPlayer getPlayer(Player player) {
        BingoPlayer bp = new BingoPlayer(player);
        for (BingoPlayer bingoPlayer : this.getPlayerList()) {
            if (bingoPlayer.equals(bp)) {
                return bingoPlayer;
            }
        }
        return null;
    }

    /**
     * @return {@code true} if the game is on Waiting Mode, or {@code false} if not
     */
    public boolean isWaitingToStart() {
        return this.waitingToStart;
    }

    /**
     * @return {@code true} if the game has already started, or {@code false} if not
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Adds a player to the Game Playerlist
     *
     * @param player Player to add into the game
     * @return {@code true} if the player was added succesfully, of {@code false} if not
     */
    public boolean addPlayer(Player player) {
        BingoPlayer bingoPlayer = new BingoPlayer(player);
        return addPlayer(bingoPlayer);
    }

    /**
     * Adds a player to the Game Playerlist
     *
     * @param bingoPlayer Player to add into the game
     * @return {@code true} if the player was added succesfully, of {@code false} if not
     */
    public boolean addPlayer(BingoPlayer bingoPlayer) {
        if (this.players.contains(bingoPlayer)) {
            return false;
        } else {
            bingoPlayer.setGame(this);
            this.players.add(bingoPlayer);
            return true;
        }
    }

    /**
     * Removes the player from the Bingo Game player list
     *
     * @param bingoPlayer The Player to be removed
     * @return {@code true} if the player was removed successfully, or {@code false} if the player was not in the player list
     */
    public boolean removePlayer(BingoPlayer bingoPlayer) {
        for (BingoPlayer player : this.players) {
            if (player.equals(bingoPlayer)) {
                this.players.remove(player);
                return true;
            }
        }
        return false;
    }

    /**
     * @return {@link BingoOptions} of the Game
     */
    public BingoOptions getOptions() {
        return options;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    /**
     * Starts the game, giving a copy of the bingo card to each bingo player
     */
    public void startGame() {
        final LangConfig lang = this.plugin.getLangConfig();
        final Server server = Bukkit.getServer();
        final Random random = new Random();
        waitingToStart = false;
        started = true;

        if (getOptions().isRandomTeams()) {
            createRandomTeams();
        }

        for (BingoTeam bingoTeam : this.teamManager.getTeams()) {
            for (BingoPlayer bingoPlayer : bingoTeam.getMembers()) {
                Player player = bingoPlayer.getPlayer();
                if (!addPlayer(bingoPlayer)) {
                    this.plugin.getLogger().warning(String.format("%s is already on the game!", player.getName()));
                }
            }
        }

        for (Player player : server.getOnlinePlayers()) {
            if (getPlayer(player) == null) {
                getSpectatorTeam().addEntity(player);
                player.setGameMode(GameMode.SPECTATOR);
            }
        }


        // Maravillosa cuenta atras.
        new BukkitRunnable() {
            int counter = 5;
            @Override
            public void run() {
                if (counter > 0) {
                    for (Player player : server.getOnlinePlayers()) {
                        player.showTitle(Title.title(TextBuilder.minimessage(color(counter) + counter), Component.text(""), Title.Times.times(Duration.ofMillis(1), Duration.ofSeconds(1), Duration.ofMillis(1))));
                        player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);
                    }
                    counter--;
                } else {
                    for (Player player : server.getOnlinePlayers()) {
                        player.showTitle(Title.title(TextBuilder.minimessage(lang.game("game_starts")), Component.text(""), Title.Times.times(Duration.ofMillis(1), Duration.ofSeconds(3), Duration.ofMillis(1))));
                        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1.0f, 1.0f);
                        player.removePotionEffect(PotionEffectType.SLOWNESS);
                        player.removePotionEffect(PotionEffectType.BLINDNESS);
                        startTimer();
                    }
                    this.cancel();
                }
            }
            private String color(int counter) {
                if (counter == 5) return "<dark_green>";
                if (counter == 4) return "<green>";
                if (counter == 3) return "<yellow>";
                if (counter == 2) return "<gold>";
                if (counter == 1) return "<red>";
                return "";
            }
        }.runTaskTimer(this.plugin, 60, 20);

        // Aplicamos algunas de las normas al mundo segun las opciones del bingo
        for (World world : server.getWorlds()) {
            world.setTime(0);
            world.setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(GameRule.NATURAL_REGENERATION, !this.options.isUhcmode());
            world.setHardcore(this.options.isHardcore());
            world.setPVP(this.options.isPvp());
            world.setGameRule(GameRule.KEEP_INVENTORY, this.options.isKeepInventory());
        }

        // Iniciar la partida para todos los jugadores que se han unido al bingo.
        this.players.forEach(bingoPlayer -> {

            Player player = bingoPlayer.getPlayer();

            // Generar una posicion aleatoria en el mundo, dentro del radio de Spread Distance
            double x = random.nextDouble(-this.options.getSpreadDistance(), this.options.getSpreadDistance());
            double z = random.nextDouble(-this.options.getSpreadDistance(), this.options.getSpreadDistance());

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
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 300, 255, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 255, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 255, false, false, false));

            player.setGameMode(GameMode.SURVIVAL);
            bingoPlayer.setPersonalCard(this.bingoCard.copy());
            player.give(new ItemStack(Material.OAK_BOAT), BingoCardItem.item());
        });

        this.plugin.getConfig().set("last-bingo-seed-played", this.bingoCard.getSeed());
        this.plugin.saveConfig();

    }

    /**
     * Finishes the game with no winners
     */
    public void finishGame() {
        final LangConfig lang = this.plugin.getLangConfig();
        final Server server = Bukkit.getServer();
        this.started = false;

        resetWorldRules();

        for (Player player : server.getOnlinePlayers()) {
            World overworld = server.getWorlds().getFirst();
            Location pos = new Location(overworld, 0.5, overworld.getHighestBlockYAt(0,0)+1, 0.5);
            player.teleport(pos);
            player.sendMessage(TextBuilder.error(lang.game("game_stopped")));
            player.setRespawnLocation(pos, true);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
            waitingToStart = true;
        }
        this.players.clear();
    }


    /**
     * Finishes the game, setting the winner to the player with most points
     *
     * @see #finishGame(BingoPlayer)
     */
    public void finishGameForTime() {
        BingoPlayer winner = null;
        int maxMarked = 0;

        for (BingoPlayer player : this.players) {
            int markedCount = player.getPersonalCard().getMarkedItemCount();
            if (winner == null || markedCount > maxMarked) {
                winner = player;
                maxMarked = markedCount;
            }
        }

        finishGame(winner);
    }

    /**
     * Finishes the game announcing the winner of the match, and resetting all the World parameters from game´s {@link BingoOptions}
     *
     * @param winner Winner of the Game
     */
    public void finishGame(BingoPlayer winner) {
        final LangConfig lang = this.plugin.getLangConfig();
        final Server server = Bukkit.getServer();
        this.started = false;

        resetWorldRules();

        for (Player player : server.getOnlinePlayers()) {
            player.showTitle(Title.title(
                    TextBuilder.minimessage(lang.game("win.title").replace("%player%", winner.getPlayer().getName())),
                    TextBuilder.minimessage(lang.game("win.subtitle").replace("%player%", winner.getPlayer().getName())),
                    Title.Times.times(Duration.ofMillis(1), Duration.ofSeconds(5), Duration.ofMillis(1))));
            player.sendMessage(TextBuilder.info(lang.game("player_won_points")
                    .replace("%player%", winner.getPlayer().getName())
                    .replace("%count%", String.valueOf(winner.getPersonalCard().getMarkedItemCount()))));
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.AMBIENT, 0.8f, 1.0f);
        }

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if (counter < 15) {
                    for (Player player : server.getOnlinePlayers()) {
                        if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                            World world = player.getWorld();
                            Firework firework = world.spawn(player.getLocation(),  Firework.class);
                            firework.setTicksToDetonate(30);
                        }
                    }
                    counter++;
                } else {
                    for (Player player : server.getOnlinePlayers()) {
                        World overworld = server.getWorlds().getFirst();
                        Location pos = new Location(overworld, 0, overworld.getHighestBlockYAt(0,0, HeightMap.MOTION_BLOCKING)+1, 0);
                        player.teleport(pos);
                        if (player.getGameMode().equals(GameMode.SPECTATOR)) player.setGameMode(GameMode.ADVENTURE);
                        waitingToStart = true;
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 0, 20);
    }

    /**
     * Sequence of actions that are repeatedly executing while on Waiting Mode
     */
    public void waitingSequence() {
        final LangConfig lang = this.plugin.getLangConfig();
        final World overworld = server.getWorlds().getFirst();
        final Location center = new Location(overworld, 0.5, overworld.getHighestBlockYAt(0,0, HeightMap.MOTION_BLOCKING)+1, 0.5);

        overworld.setDifficulty(Difficulty.PEACEFUL);
        overworld.setTime(6000);
        overworld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        for (Player player : server.getOnlinePlayers()) {

            player.sendActionBar(TextBuilder.minimessage(lang.game("waiting_to_start")));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 60, 255, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 255, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 255, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 255, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, 255, false, false, false));

            if (player.getLocation().distance(center) > this.plugin.getConfig().getInt("spawn-max-distance")) {
                if (!player.getGameMode().isInvulnerable()) {
                    player.teleport(center);
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT, 1.0f, 0.1f);
                    player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.U_CANT_ESCAPE)));
                }
            }
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                player.setGameMode(GameMode.ADVENTURE);
            }
        }
    }

    private @Nullable Team getTeam(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard score = manager.getMainScoreboard();
        if (player != null) return score.getEntityTeam(player);
        return null;
    }

    private void startTimer() {
        new BukkitRunnable() {

            private int totalSeconds = 0;
            private final Server server = Bukkit.getServer();

            @Override
            public void run() {
                if (!started) this.cancel();

                final LangConfig lang = plugin.getLangConfig();
                final int hours = totalSeconds / 3600;
                final int minutes = (totalSeconds % 3600) / 60;
                final int seconds = totalSeconds % 60;

                for (Player player : server.getOnlinePlayers()) {
                    if (lang.game("total_game_time") != null) {
                        player.sendActionBar(TextBuilder.minimessage(lang.game("total_game_time")
                                .replace("%h%", String.format("%02d", hours))
                                .replace("%m%", String.format("%02d", minutes))
                                .replace("%s%", String.format("%02d", seconds))));
                    } else {
                        player.sendActionBar(TextBuilder.minimessage("<red>Couln´t load bingo.game.total_game_time"));
                    }
                }

                if (getOptions().isTimeLimit()) {
                    if (getOptions().getMaxTime() <= totalSeconds) {
                        finishGameForTime();
                        this.cancel();
                    }
                }
                totalSeconds++;
            }
        }.runTaskTimer(this.plugin, 0, 20);
    }

    /**
     * Sets the Spectators Team
     * @param spectatorTeam
     */
    public void setSpectatorTeam(Team spectatorTeam) {
        this.spectatorTeam = spectatorTeam;
    }

    /**
     * @return Spectator´s {@link Team}
     */
    public Team getSpectatorTeam() {
        return spectatorTeam;
    }

    private void resetWorldRules() {
        final Server server = Bukkit.getServer();
        for (World world : server.getWorlds()) {
            world.setGameRule(GameRule.NATURAL_REGENERATION, true);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setHardcore(false);
            world.setPVP(true);
        }
    }

    private void createRandomTeams() {
        int teamSize = this.getOptions().getTeamSize();
        int teamNumber = 1;

        List<Player> onlinePlayers = new ArrayList<>(this.server.getOnlinePlayers());
        Collections.shuffle(onlinePlayers);

        for (int i = 0; i < onlinePlayers.size(); i += teamSize) {
            BingoTeam bingoTeam = new BingoTeam();
            bingoTeam.setColor(BingoTeam.randomColor());
            bingoTeam.setPrefix(i + "");
            bingoTeam.setTeamName(this.plugin.getConfig().getString("default-team-name").replace("%number%", String.valueOf(teamNumber)));
            for (int j = 0; j < teamSize && i + j < onlinePlayers.size(); j++) {
                Player player = onlinePlayers.get(i + j);
                if (getPlayer(player) == null) {
                    BingoPlayer bingoPlayer = new BingoPlayer(player);
                    if (j == 0) bingoTeam.setOwner(bingoPlayer);
                    bingoTeam.addMember(bingoPlayer);
                } else {
                    BingoPlayer bingoPlayer = getPlayer(player);
                    if (bingoPlayer.getTeam() == null) {
                        if (j == 0) bingoTeam.setOwner(bingoPlayer);
                        bingoTeam.addMember(bingoPlayer);
                    }
                }
            }
            this.teamManager.registerTeam(bingoTeam);
            teamNumber++;
        }
    }
}