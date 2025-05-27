package net.espectralgames.bingoEspectral.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Menu extends InventoryHolder {
    void click(Player player, int slot, ClickType clickType);

    void setItem(int slot, ItemStack itemStack);

    void setItem(int slot, ItemStack itemStack, BiConsumer<Player, ClickType> action);

    void onSetItems();

    default void open(Player player) {
        onSetItems();
        player.openInventory(getInventory());
    }
}
