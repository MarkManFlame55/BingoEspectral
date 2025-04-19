package net.espectralgames.bingoEspectral.bingo;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.options.BingoType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class BingoCard {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private boolean generated = false;
    private String seed = "";


    // Card Dimensions
    private final int SIZE = 5; // Para crear una carta de 5x5


    // Actual card on the array
    private final BingoBox[][] bingoCard = new BingoBox[SIZE][SIZE];


    public BingoCard() {

    }

    // Just for the copy() method :P
    private BingoCard(BingoBox[][] card) {
        for (int i = 0; i < this.bingoCard.length; i++) {
            for (int j = 0; j < this.bingoCard[i].length; j++) {
                this.bingoCard[i][j] = new BingoBox(card[i][j].getMaterial());
            }
        }
    }


    /**
     * @return {@code true} if has the bingo card is generated with {@link #generate(String)} method, or false if not
     */
    public boolean isGenerated() {
        return generated;
    }

    /**
     * Fills the bingo card based on the Bingo Seed passed as parameter
     *
     * @param seed String id of the card on cards.yml
     * @return Filled {@link BingoCard} with the seed`s items, or {@code null} if the generation failed
     */

    public BingoCard generate(String seed) {
        final YamlConfiguration cardsConfig = this.plugin.getCardsConfig();
        final ConfigurationSection card = cardsConfig.getConfigurationSection("bingo.cards." + seed);
        if (card != null) {

            this.plugin.getLogger().info("Generating Card");

            if (card.contains("pool"))  {
                final List<String> carta = cardsConfig.getStringList("bingo.cards." + seed + ".pool");

                if (carta.size() < 25) {
                    this.plugin.getLogger().severe("Missing Elements!");
                    return null;
                }

                for (int i = 0; i < this.bingoCard.length; i++) {
                    for (int j = 0; j < this.bingoCard[i].length; j++) {
                        String casilla = getRandomElement(carta);
                        while (casilla.isEmpty()) casilla = getRandomElement(carta);
                        if (Material.getMaterial(casilla) != null) {
                            this.bingoCard[i][j] = new BingoBox(Material.getMaterial(casilla));
                        } else {
                            this.plugin.getLogger().severe("Interrupting Card Generation");
                            this.plugin.getLogger().severe(String.format("Wrontg Element: %s", casilla));
                            return null;
                        }
                    }
                }
                generated = true;
                setSeed(seed);
                return this;
            } else {
                if (isValidCard(card)) {
                    final List<String> row1 = cardsConfig.getStringList("bingo.cards." + seed + ".row1");
                    final List<String> row2 = cardsConfig.getStringList("bingo.cards." + seed + ".row2");
                    final List<String> row3 = cardsConfig.getStringList("bingo.cards." + seed + ".row3");
                    final List<String> row4 = cardsConfig.getStringList("bingo.cards." + seed + ".row4");
                    final List<String> row5 = cardsConfig.getStringList("bingo.cards." + seed + ".row5");

                    if (!fillRow(0, row1)) {
                        this.plugin.getLogger().warning("Interrupting Card Generation");
                        this.plugin.getLogger().warning("Wrong Element at row1");
                        return null;
                    }
                    if (!fillRow(1, row2)) {
                        this.plugin.getLogger().warning("Interrupting Card Generation");
                        this.plugin.getLogger().warning("Wrong Element at row2");
                        return null;
                    }
                    if (!fillRow(2, row3)) {
                        this.plugin.getLogger().warning("Interrupting Card Generation");
                        this.plugin.getLogger().warning("Wrong Element at row3");
                        return null;
                    }
                    if (!fillRow(3, row4)) {
                        this.plugin.getLogger().warning("Interrupting Card Generation");
                        this.plugin.getLogger().warning("Wrong Element at row4");
                        return null;
                    }
                    if (!fillRow(4, row5)) {
                        this.plugin.getLogger().warning("Interrupting Card Generation");
                        this.plugin.getLogger().warning("Wrong Element at row5");
                        return null;
                    }
                    this.plugin.getLogger().info("Card Generated Successfully");
                    generated = true;
                    setSeed(seed);
                    return this;
                }
            }
        }
        return null;
    }


    private boolean fillRow(int row, @NotNull List<String> contents) {
        if (Material.getMaterial(contents.getFirst()) != null) {
            this.bingoCard[row][0] = new BingoBox(Material.getMaterial(contents.getFirst()));
        } else {
            return false;
        }
        if (Material.getMaterial(contents.get(1)) != null) {
            this.bingoCard[row][1] = new BingoBox(Material.getMaterial(contents.get(1)));
        } else {
            return false;
        }
        if (Material.getMaterial(contents.get(2)) != null) {
            this.bingoCard[row][2] = new BingoBox(Material.getMaterial(contents.get(2)));
        } else {
            return false;
        }
        if (Material.getMaterial(contents.get(3)) != null) {
            this.bingoCard[row][3] = new BingoBox(Material.getMaterial(contents.get(3)));
        } else {
            return false;
        }
        if (Material.getMaterial(contents.get(4)) != null) {
            this.bingoCard[row][4] = new BingoBox(Material.getMaterial(contents.get(4)));
        } else {
            return false;
        }
        return true;
    }

    /**
     *  Creates a copy of this Bingo Card
     *
     * @return a copy of this Bingo Card
     */
    public BingoCard copy() {
        return new BingoCard(this.bingoCard);
    }

    private boolean isValidCard(@NotNull ConfigurationSection card) {
        return card.contains("row1") && card.contains("row2") && card.contains("row3") && card.contains("row4") && card.contains("row5");
    }

    private String getRandomElement(@NotNull List<String> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        String value = list.get(index);
        list.set(index, "");
        return value;
    }

    /**
     * @return the number of marked items on the card
     */
    public int getMarkedItemCount() {
        int markedItems = 0;
        for (BingoBox[] elements : this.bingoCard) {
            for (BingoBox element : elements) {
                if (element.isMarked()) {
                    markedItems++;
                }
            }
        }
        return markedItems;
    }

    public boolean isMarked(Material material) {
        for (int i = 0; i < this.bingoCard.length; i++) {
            for (int j = 0; j < this.bingoCard[i].length; j++) {
                final BingoBox element = this.getElementAt(i,j);
                if (element.getMaterial().equals(material)) {
                    return element.isMarked();
                }
            }
        }
        return false;
    }

    /**
     * Marks down an item in the card
     *
     * @param material Material to be marked down in the card
     * @return {@code true} if the material was successfully marked down in the card, or {@code false} if not
     */
    public boolean discardItem(Material material) {
        for (int i = 0; i < this.bingoCard.length; i++) {
            for (int j = 0; j < this.bingoCard[i].length; j++) {
                if (material.equals(this.bingoCard[i][j].getMaterial())) {
                    if (!this.bingoCard[i][j].isMarked()) {
                        this.bingoCard[i][j].setMarked(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the entirety of the card is completed
     *
     * @return {@code true} if the card is completed
     */
    public boolean isCompleted()  {
        for (BingoBox[] bingoBoxes : this.bingoCard) {
            for (BingoBox box : bingoBoxes) {
                if (!box.isMarked()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the {@link BingoBox} on the given position
     *
     * @param x X Position on the Card
     * @param y Y Position on the Card
     * @return Bingo box on that position
     */
    public BingoBox getElementAt(int x, int y) {
        return this.bingoCard[y][x];
    }

    /**
     * Checks if the actual card has Bingo according to the {@link BingoType}
     * @param type BingoType for the card to follow
     *  @return {@code true} if there's a bingo, or {@code false} if not
     */
    public boolean hasBingo(BingoType type) {
        switch (type) {
            case ALL -> {
                return checkAll();
            }
            case HORIZONTAL -> {
                return checkHorizontals();
            }
            case VERTICAL -> {
                return checkVerticals();
            }
            case DIAGONAL -> {
                return checkDiagonals();
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Checks for every type of Bingo
     * @return {@code true} if there is some kind of bingo, or {@code false} if not.
     */
    public boolean checkAll() {
        return checkHorizontals() || checkVerticals() || checkDiagonals();
    }

    /**
     * Checks all rows on the Bingo Card looking for a Horizontal Bingo!
     * @return {@code true} if there`s a horizontal bingo, or {@code false} if not
     */
    public boolean checkHorizontals() {
        for (int i = 0; i < SIZE; i++) {
            if (isFullRow(this.bingoCard, i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all columns on the Bingo Card looking for a Vertical Bingo!
     * @return {@code true} if there`s a vertical bingo, or {@code false} if not
     */
    public boolean checkVerticals() {
        for (int i = 0; i < SIZE; i++) {
            if (isFullColumn(this.bingoCard, i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all diagonal lines on the Bingo Card looking for a Diagonal Bingo!
     * @return {@code true} if there`s a diagonal bingo, or {@code false} if not
     */
    public boolean checkDiagonals() {
        return isLeftToRightDiagonalFull(this.bingoCard) || isRightToLeftDiagonalFull(this.bingoCard);
    }

    private boolean isFullRow(BingoBox[][] bingoCard, int row) {
        for (int col = 0; col < bingoCard.length; col++) {
            if (!bingoCard[row][col].isMarked()) {
                return false;
            }
        }
        return true;
    }

    private boolean isFullColumn(BingoBox[][] bingoCard, int col) {
        for (int row = 0; row < bingoCard.length; row++) {
            if (!bingoCard[row][col].isMarked()) {
                return false;
            }
        }
        return true;
    }

    private boolean isLeftToRightDiagonalFull(BingoBox[][] bingoCard) {
        for (int i = 0; i < bingoCard.length; i++) {
            if (!bingoCard[i][i].isMarked()) {
                return false;
            }
        }
        return true;
    }

    private boolean isRightToLeftDiagonalFull(BingoBox[][] bingoCard) {
        for (int i = 0; i < bingoCard.length; i++) {
            if (!bingoCard[i][bingoCard.length - 1 - i].isMarked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the seed used to generate the bingo card
     *
     * @return the seed of the BingoCard
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Sets the stored seed ({@link #generate(String)} method doesn´t use the seed passed here)
     *
     * @param seed String
     */
    public void setSeed(String seed) {
        this.seed = seed;
    }
}
