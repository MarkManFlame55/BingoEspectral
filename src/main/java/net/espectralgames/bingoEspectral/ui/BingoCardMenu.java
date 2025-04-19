package net.espectralgames.bingoEspectral.ui;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoBox;
import net.espectralgames.bingoEspectral.bingo.BingoCard;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BingoCardMenu extends SimpleMenu {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoCard bingoCard;
    private final BingoGame bingoGame = this.plugin.getBingoGame();
    private final LangConfig lang = this.plugin.getLangConfig();

    public BingoCardMenu(BingoCard bingoCard) {
        super(Rows.FIVE, BingoEspectral.getPlugin().getLangConfig().getString("bingo.ui.card_title"));
        this.bingoCard = bingoCard;
    }
    public BingoCardMenu(BingoCard bingoCard, String titleKey) {
        super(Rows.FIVE, titleKey);
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
                setItem(i, unmarkedSlots(element.getMaterial()));
            }
        }

        for (int i = 11; i < 16; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-11, 1);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, unmarkedSlots(element.getMaterial()));
            }
        }

        for (int i = 20; i < 25; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-20, 2);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, unmarkedSlots(element.getMaterial()));
            }
        }

        for (int i = 29; i < 34; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-29, 3);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, unmarkedSlots(element.getMaterial()));
            }
        }

        for (int i = 38; i < 43; i++) {
            BingoBox element = this.bingoCard.getElementAt(i-38, 4);
            if (element.isMarked()) {
                setItem(i, markedSlot(element.getMaterial()));
            } else {
                setItem(i, unmarkedSlots(element.getMaterial()));
            }
        }
    }

    private ItemStack markedSlot(Material material) {
        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(MiniMessage.miniMessage().deserialize("<green><lang:" + material.getItemTranslationKey() + ">").decoration(TextDecoration.ITALIC, false));
        itemMeta.setEnchantmentGlintOverride(true);
        final List<Component> lore = new ArrayList<>();
        lore.add(TextBuilder.minimessage(lang.ui("found_by")));
        for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
            if (bingoPlayer.getPersonalCard().isMarked(material)) {
                lore.add(TextBuilder.minimessage("<white>- <gray>" + bingoPlayer.getName()));
            }
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack unmarkedSlots(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        final List<Component> lore = new ArrayList<>();
        lore.add(TextBuilder.minimessage(lang.ui("found_by")));
        for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
            if (bingoPlayer.getPersonalCard().isMarked(material)) {
                lore.add(TextBuilder.minimessage("<white>- <gray>" + bingoPlayer.getName()));
            }
        }
        itemMeta.lore(lore);

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
