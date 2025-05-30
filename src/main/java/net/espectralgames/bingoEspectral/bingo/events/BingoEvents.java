package net.espectralgames.bingoEspectral.bingo.events;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.bingo.options.BingoType;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.item.BingoCardItem;
import net.espectralgames.bingoEspectral.ui.Menu;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BingoEvents implements Listener {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();
    private final Server server = Bukkit.getServer();

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (this.bingoGame.isStarted()) {
                final BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
                final ItemStack itemStack = e.getItem().getItemStack();
                final Material material = itemStack.getType();

                if (bingoPlayer == null) {
                    return;
                }

                if (itemStack.isSimilar(BingoCardItem.item())) {
                    return;
                }

                if (bingoPlayer.getPersonalCard().discardItem(material)) {
                    BingoTeam bingoTeam = bingoPlayer.getTeam();

                    for (BingoPlayer teammate : bingoTeam.getMembers()) {
                        teammate.getPersonalCard().discardItem(material);
                    }

                    if (this.bingoGame.getOptions().isRemoveMarkedItems()) {
                        e.setCancelled(true);
                        e.getItem().remove();
                        if (itemStack.getAmount() - 1 != 0) player.give(itemStack.subtract());
                    }
                    playerFoundItem(bingoPlayer, material);
                }
            }
        }
    }




    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getHolder() instanceof Menu) {
            return;
        }

        if (e.getWhoClicked() instanceof Player player) {
            if (this.bingoGame.isStarted()) {
                final BingoPlayer bingoPlayer = this.bingoGame.getPlayer(player);
                if (bingoPlayer == null) return;
                final ItemStack itemStack = e.getCurrentItem();
                if (itemStack != null && !itemStack.isSimilar(BingoCardItem.item())) {
                    final Material material = itemStack.getType();
                    if (bingoPlayer.getPersonalCard().discardItem(material)) {
                        BingoTeam bingoTeam = bingoPlayer.getTeam();
                        for (BingoPlayer teammate : bingoTeam.getMembers()) {
                            teammate.getPersonalCard().discardItem(material);
                        }

                        if (this.bingoGame.getOptions().isRemoveMarkedItems()) {
                            itemStack.subtract();
                            if (e.getClickedInventory() instanceof CraftingInventory craftingInventory) {
                                for (@Nullable ItemStack item : craftingInventory.getMatrix()) {
                                    if (item != null) {
                                        item.setAmount(item.getAmount()-1);
                                    }
                                }
                            }
                        }
                        playerFoundItem(bingoPlayer, material);
                    }
                }
            }
        }
    }

    private void playerFoundItem(BingoPlayer bingoPlayer, Material material) {
        final LangConfig lang = this.plugin.getLangConfig();
        final Player player = bingoPlayer.getPlayer();

        for (Player p : server.getOnlinePlayers()) {
            p.sendMessage(TextBuilder.success(lang.game("item_found").replace("%player%", player.getName()).replace("%item%", "<lang:" + material.getItemTranslationKey() + ">")));
            p.playSound(p, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 1.0f, 1.0f);
        }
        bingoPlayer.addPoints(this.bingoGame.getOptions().getPointsPerItem());
        if (this.bingoGame.getOptions().isFullCard()) {

            List<String> nuevasLineas = bingoPlayer.getPersonalCard().getNewCompletedLines();

            if (!nuevasLineas.isEmpty()) {
                for (Player p : server.getOnlinePlayers()) {
                    p.sendMessage(TextBuilder.success(lang.game("bingo_scored").replace("%player%", player.getName())));
                    p.playSound(p, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 1.0f, 1.0f);
                }
                bingoPlayer.addPoints(this.bingoGame.getOptions().getPointsPerBingo());
            }

            if (bingoPlayer.getPersonalCard().isCompleted()) {
                this.bingoGame.finishGame(bingoPlayer.getTeam());
            }
        } else {
            if (bingoPlayer.getPersonalCard().hasBingo(this.bingoGame.getOptions().getType())) {
                bingoPlayer.addPoints(this.bingoGame.getOptions().getPointsPerBingo());
                this.bingoGame.finishGame(bingoPlayer.getTeam());
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (this.bingoGame.getOptions().isHardcore()) {
            final LangConfig lang = this.plugin.getLangConfig();
            final Player player = e.getPlayer();
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(TextBuilder.info(lang.game("disqualified")));
            for (Player p : this.server.getOnlinePlayers()) {
                p.playSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, SoundCategory.AMBIENT, 1.0f, 0.1f);
            }
            final Team specs = this.bingoGame.getSpectatorTeam();
            if (specs != null) {
                specs.addEntity(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final LangConfig lang = this.plugin.getLangConfig();
        if (this.bingoGame.isStarted()) {
            final Player player = e.getPlayer();
            if (this.bingoGame.getPlayer(player) == null) {
                player.setGameMode(GameMode.SPECTATOR);

                final Team specs = this.bingoGame.getSpectatorTeam();
                if (specs != null) {
                    specs.addEntity(player);
                }

                player.sendMessage(TextBuilder.info(lang.game("game_already_started")));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (this.bingoGame.isStarted()) {
            final Player player = e.getPlayer();
            if (this.bingoGame.getPlayer(player) != null) {
                if (!this.bingoGame.getOptions().isHardcore() && !this.bingoGame.getOptions().isKeepInventory()) player.give(BingoCardItem.item());
            }
        }
    }

}
