package net.espectralgames.bingoEspectral.item;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.ui.BingoCardMenu;
import net.espectralgames.bingoEspectral.utils.ErrorMessage;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BingoCardItem implements Listener {

    private static final BingoEspectral plugin = BingoEspectral.getPlugin();
    private static final BingoGame bingoGame = plugin.getBingoGame();

    public static ItemStack item() {
        final LangConfig lang = plugin.getLangConfig();
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.game("bingo_card_item.item_name")));
        itemMeta.setRarity(ItemRarity.EPIC);
        itemMeta.setMaxStackSize(1);
        itemMeta.setEnchantmentGlintOverride(true);
        List<Component> lore = new ArrayList<>();
        lore.add(TextBuilder.minimessage(lang.game("bingo_card_item.description")));
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e) {
        final LangConfig lang = plugin.getLangConfig();
        if (e.getAction().isRightClick()) {
            ItemStack itemStack = e.getItem();
            if (itemStack != null && itemStack.isSimilar(item())) {
                Player player = e.getPlayer();
                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.AMBIENT, 1.0f, 1.0f);
                player.swingMainHand();
                BingoPlayer bingoPlayer = bingoGame.getPlayer(player);
                if (bingoPlayer != null && bingoPlayer.getPersonalCard() != null) {
                    new BingoCardMenu(bingoPlayer.getPersonalCard()).open(player);
                } else {
                    player.sendMessage(TextBuilder.error(lang.error(ErrorMessage.CANT_OPEN_BINGO_CARD)));
                }

            }
        }
    }
}
