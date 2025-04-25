package net.espectralgames.bingoEspectral.bingo.team;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class TeamInvite {
    private final UUID owner;
    private final BukkitTask task;

    public TeamInvite(UUID owner, BukkitTask task) {
        this.owner = owner;
        this.task = task;
    }

    public UUID getOwner() {
        return owner;
    }

    public BukkitTask getTask() {
        return task;
    }
}
