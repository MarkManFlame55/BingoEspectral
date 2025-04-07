package net.espectralgames.bingoEspectral.bingo;

import org.bukkit.Material;

public class BingoBox {

    private Material material;
    private boolean marked = false;

    public BingoBox(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isMarked() {
        return marked;
    }
}
