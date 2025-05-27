package net.espectralgames.bingoEspectral.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class SimpleMenu implements Menu {

    private final Map<Integer, BiConsumer<Player, ClickType>> actions = new HashMap<>();
    private final Inventory inventory;

    public SimpleMenu(Rows rows, String title) {
        this.inventory = Bukkit.createInventory(this, rows.getSize(), MiniMessage.miniMessage().deserialize(title));

    }

    @Override
    public void click(Player player, int slot, ClickType clickType) {
        final BiConsumer<Player, ClickType> action = this.actions.get(slot);
        if (action != null) action.accept(player, clickType);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        setItem(slot, itemStack, (player, clickType) -> {});
    }

    @Override
    public void setItem(int slot, ItemStack itemStack, BiConsumer<Player, ClickType> action) {
        this.actions.put(slot, action);
        getInventory().setItem(slot, itemStack);
    }

    @Override
    public abstract void onSetItems();

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public enum Rows {

        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6);

        private final int size;

        Rows(int rows) {
            this.size = rows * 9;
        }

        public int getSize() {
            return size;
        }
    }
}
