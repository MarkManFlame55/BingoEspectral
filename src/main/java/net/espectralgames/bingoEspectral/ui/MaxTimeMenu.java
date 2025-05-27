package net.espectralgames.bingoEspectral.ui;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MaxTimeMenu extends SimpleMenu {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame bingoGame = this.plugin.getBingoGame();

    public MaxTimeMenu() {
        super(Rows.THREE, BingoEspectral.getPlugin().getLangConfig().getString("bingo.ui.max_time"));
    }

    @Override
    public void onSetItems() {

        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, empty());
        }

        setItem(10, simpleMenuItem(Material.BLUE_CONCRETE, "<blue>15min"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(minutes(15));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });

        setItem(11, simpleMenuItem(Material.LIGHT_BLUE_CONCRETE, "<aqua>30min"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(minutes(30));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });

        setItem(12, simpleMenuItem(Material.LIME_CONCRETE, "<green>1h"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(hours(1));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });

        setItem(13, simpleMenuItem(Material.YELLOW_CONCRETE, "<yellow>1h30min"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(hours(1) + minutes(30));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });

        setItem(14, simpleMenuItem(Material.ORANGE_CONCRETE, "<gold>2h"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(hours(2));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });

        setItem(15, simpleMenuItem(Material.RED_CONCRETE, "<red>2h30min"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(hours(2) + minutes(30));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });

        setItem(16, simpleMenuItem(Material.GRAY_CONCRETE, "<dark_red>3h"), (player, clickType) -> {
            this.bingoGame.getOptions().setMaxTime(hours(3));
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f ,1.0f);
            new BingoOptionsMenu().open(player);
        });
    }


    private ItemStack simpleMenuItem(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(displayName));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private int minutes(int minutes) {
        return minutes * 60;
    }
    private int hours(int hours) {
        return hours * 3600;
    }

    private ItemStack empty() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setHideTooltip(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
