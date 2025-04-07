package net.espectralgames.bingoEspectral.bingo;

import org.bukkit.entity.Player;

public class BingoPlayer {

    private final Player player;
    private BingoGame game;
    private BingoCard personalCard;

    public BingoPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BingoGame getGame() {
        return game;
    }

    public void setGame(BingoGame game) {
        this.game = game;
    }
    public BingoCard getPersonalCard() {
        return this.personalCard;
    }

    public void setPersonalCard(BingoCard personalCard) {
        this.personalCard = personalCard;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BingoPlayer bingoPlayer) {
            return bingoPlayer.getPlayer().getUniqueId().equals(this.getPlayer().getUniqueId());
        }
        return false;
    }
}
