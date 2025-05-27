package net.espectralgames.bingoEspectral.ui.listeners;

import net.espectralgames.bingoEspectral.ui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MenuListeners implements Listener {

    @EventHandler
    public void onCLick(InventoryClickEvent e) {
        final Inventory clickedInv = e.getClickedInventory();

        if (clickedInv == null) {
            return;
        }

        if (!(clickedInv.getHolder() instanceof final Menu menu)) {
            return;
        }
        e.setCancelled(true);
        menu.click((Player) e.getWhoClicked(), e.getSlot(), e.getClick());
    }
}
