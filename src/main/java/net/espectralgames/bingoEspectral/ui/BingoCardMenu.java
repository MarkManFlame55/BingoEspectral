package net.espectralgames.bingoEspectral.ui;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoBox;
import net.espectralgames.bingoEspectral.bingo.BingoCard;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.BingoPlayer;
import net.espectralgames.bingoEspectral.bingo.team.BingoTeam;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BingoCardMenu extends SimpleMenu {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoCard bingoCard;
    private final BingoGame bingoGame = this.plugin.getBingoGame();
    private BingoPlayer player;
    private final LangConfig lang = this.plugin.getLangConfig();

    public BingoCardMenu(BingoCard bingoCard) {
        super(Rows.FIVE, BingoEspectral.getPlugin().getLangConfig().getString("bingo.ui.card_title"));
        this.bingoCard = bingoCard;
    }

    public BingoCardMenu(BingoCard bingoCard, BingoPlayer bingoPlayer) {
        super(Rows.FIVE, BingoEspectral.getPlugin().getLangConfig().getString("bingo.ui.card_title"));
        this.bingoCard = bingoCard;
        this.player = bingoPlayer;
    }

    public BingoCardMenu(BingoCard bingoCard, String titleKey, BingoPlayer bingoPlayer) {
        super(Rows.FIVE, titleKey);
        this.bingoCard = bingoCard;
    }



    @Override
    public void onSetItems() {

        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, empty());
        }

        setItem(0, topTeams());

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
        final List<BingoTeam> teamWithItem = new ArrayList<>();
        final List<Component> lore = new ArrayList<>();

        if (this.player == null) {
            for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
                if (bingoPlayer.getPersonalCard().isMarked(material)) {
                    if (!teamWithItem.contains(bingoPlayer.getTeam())) {
                        teamWithItem.add(bingoPlayer.getTeam());
                    }
                }
            }
        } else {
            for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
                if (!bingoPlayer.equals(this.player)) {
                    if (bingoPlayer.getPersonalCard().isMarked(material)) {
                        if (!teamWithItem.contains(bingoPlayer.getTeam())) {
                            teamWithItem.add(bingoPlayer.getTeam());
                        }
                    }
                }
            }
        }

        if (!teamWithItem.isEmpty()) {
            lore.add(TextBuilder.minimessage(lang.ui("found_by")));
            for (BingoTeam bingoTeam : teamWithItem) {
                lore.add(TextBuilder.minimessage("<white>- ").append(TextBuilder.minimessage("[" + bingoTeam.getPrefix() + "]").color(bingoTeam.getColor())));
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

        final List<BingoTeam> teamWithItem = new ArrayList<>();
        final List<Component> lore = new ArrayList<>();

        if (this.player == null) {
            for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
                if (bingoPlayer.getPersonalCard().isMarked(material)) {
                    if (!teamWithItem.contains(bingoPlayer.getTeam())) {
                        teamWithItem.add(bingoPlayer.getTeam());
                    }
                }
            }
        } else {
            for (BingoPlayer bingoPlayer : this.bingoGame.getPlayerList()) {
                if (!bingoPlayer.equals(this.player)) {
                    if (bingoPlayer.getPersonalCard().isMarked(material)) {
                        if (!teamWithItem.contains(bingoPlayer.getTeam())) {
                            teamWithItem.add(bingoPlayer.getTeam());
                        }
                    }
                }
            }
        }

        if (!teamWithItem.isEmpty()) {
            lore.add(TextBuilder.minimessage(lang.ui("found_by")));
            for (BingoTeam bingoTeam : teamWithItem) {
                lore.add(TextBuilder.minimessage("<white>- ").append(TextBuilder.minimessage("[" + bingoTeam.getPrefix() + "]").color(bingoTeam.getColor())));
            }
        }

        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack topTeams() {
        ItemStack itemStack = new ItemStack(Material.SUNFLOWER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage("<gold><b>Top Points"));
        List<Component> lore = new ArrayList<>();
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

            lore.add(numberComponent
                    .append(TextBuilder.minimessage(bingoTeam.getTeamName()).color(bingoTeam.getColor()))
                    .append(TextBuilder.minimessage("<white> - <aqua>" + bingoTeam.getTeamPoints())));
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
