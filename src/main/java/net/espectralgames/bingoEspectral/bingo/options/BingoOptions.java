package net.espectralgames.bingoEspectral.bingo.options;

public class BingoOptions {

    private boolean hardcore = false;
    private boolean timeLimit = false;
    private int spreadDistance = 2000;
    private boolean fullCard = false;
    private BingoType type = BingoType.ALL;
    private boolean pvp = false;
    private boolean uhcmode = false;
    private boolean removeMarkedItems = false;
    private boolean keepInventory = false;
    private boolean displayTime = true;
    private int maxTime = 3600;
    private int pointePerItem = 1;
    private int pointsPerBingo = 5;
    private int teamSize = 1;
    private boolean randomTeams = true;

    public void setFullCard(boolean fullCard) {
        this.fullCard = fullCard;
    }

    public void setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
    }

    public void setSpreadDistance(int spreadDistance) {
        this.spreadDistance = spreadDistance;
    }

    public void setTimeLimit(boolean timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getSpreadDistance() {
        return spreadDistance;
    }

    public boolean isFullCard() {
        return fullCard;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public boolean isTimeLimit() {
        return timeLimit;
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isUhcmode() {
        return uhcmode;
    }

    public void setUhcmode(boolean uhcmode) {
        this.uhcmode = uhcmode;
    }

    public BingoType getType() {
        return type;
    }

    public void setType(BingoType type) {
        this.type = type;
    }

    public boolean isRemoveMarkedItems() {
        return removeMarkedItems;
    }

    public void setRemoveMarkedItems(boolean removeMarkedItems) {
        this.removeMarkedItems = removeMarkedItems;
    }

    public boolean isKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(boolean displayTime) {
        this.displayTime = displayTime;
    }

    public int getPointePerItem() {
        return pointePerItem;
    }

    public void setPointePerItem(int pointePerItem) {
        this.pointePerItem = pointePerItem;
    }

    public int getPointsPerBingo() {
        return pointsPerBingo;
    }

    public void setPointsPerBingo(int pointsPerBingo) {
        this.pointsPerBingo = pointsPerBingo;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public boolean isRandomTeams() {
        return randomTeams;
    }

    public void setRandomTeams(boolean randomTeams) {
        this.randomTeams = randomTeams;
    }
}
