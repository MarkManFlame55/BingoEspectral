package net.espectralgames.bingoEspectral.ui;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoBox;
import net.espectralgames.bingoEspectral.bingo.BingoCard;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BingoCardMenu extends SimpleMenu {

    private final BingoCard bingoCard;

    public BingoCardMenu(BingoCard bingoCard) {
        super(Rows.FIVE, BingoEspectral.getPlugin().getLangConfig().getString("bingo.ui.card_title"));
        this.bingoCard = bingoCard;
    }

    @Override
    public void onSetItems() {

        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, empty());
        }

        for (int i = 2; i < 7; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-2, 0);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, new ItemStack(element.getMaterial()));
            }
        }

        for (int i = 11; i < 16; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-11, 1);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, new ItemStack(element.getMaterial()));
            }
        }

        for (int i = 20; i < 25; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-20, 2);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, new ItemStack(element.getMaterial()));
            }
        }

        for (int i = 29; i < 34; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-29, 3);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, new ItemStack(element.getMaterial()));
            }
        }

        for (int i = 38; i < 43; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-38, 4);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, new ItemStack(element.getMaterial()));
            }
        }
    }

    private ItemStack markedSlot(Material material) {
        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(MiniMessage.miniMessage().deserialize("<green><lang:" + material.getItemTranslationKey() + ">").decoration(TextDecoration.ITALIC, false));
        itemMeta.setEnchantmentGlintOverride(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack empty() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setHideTooltip(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
