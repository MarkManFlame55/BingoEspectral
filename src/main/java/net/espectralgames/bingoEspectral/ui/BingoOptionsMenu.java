package net.espectralgames.bingoEspectral.ui;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.espectralgames.bingoEspectral.bingo.BingoGame;
import net.espectralgames.bingoEspectral.bingo.options.BingoType;
import net.espectralgames.bingoEspectral.utils.LangConfig;
import net.espectralgames.bingoEspectral.utils.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BingoOptionsMenu extends SimpleMenu {

    private final BingoEspectral plugin = BingoEspectral.getPlugin();
    private final BingoGame game = this.plugin.getBingoGame();
    private final LangConfig lang = this.plugin.getLangConfig();

    public BingoOptionsMenu() {
        super(Rows.FOUR, BingoEspectral.getPlugin().getLangConfig().ui("options_title"));
    }

    @Override
    public void onSetItems() {

        for (int i = 0; i < getInventory().getSize(); i++) {
            setItem(i, empty());
        }

        setItem(10, displayTimeItem(), (player, click) -> {
            this.game.getOptions().setDisplayTime(!this.game.getOptions().isDisplayTime());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(11, timelimitItem(), (player, click) -> {
            this.game.getOptions().setTimeLimit(!this.game.getOptions().isTimeLimit());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(12, maxTimeItem(), (player, click) -> {
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);
            new MaxTimeMenu().open(player);
        });

        setItem(13, tipoBingoItem(), (player, click) -> {
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);
            switch (this.game.getOptions().getType()) {
                case ALL -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setType(BingoType.HORIZONTAL);
                    } else {
                        this.game.getOptions().setType(BingoType.DIAGONAL);
                    }
                }
                case HORIZONTAL -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setType(BingoType.VERTICAL);
                    } else {
                        this.game.getOptions().setType(BingoType.ALL);
                    }
                }
                case VERTICAL -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setType(BingoType.DIAGONAL);
                    } else {
                        this.game.getOptions().setType(BingoType.HORIZONTAL);
                    }
                }
                case DIAGONAL -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setType(BingoType.ALL);
                    } else {
                        this.game.getOptions().setType(BingoType.VERTICAL);
                    }
                }
            }
            onSetItems();
        });

        setItem(14, pvpItem(), (player, click) -> {
            this.game.getOptions().setPvp(!this.game.getOptions().isPvp());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(15, hardcoreItem(), (player, click) -> {
            this.game.getOptions().setHardcore(!this.game.getOptions().isHardcore());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        ItemStack uhcmodeItem = uhcmodeItem();
        setItem(16, uhcmodeItem, (player, click) -> {
            this.game.getOptions().setUhcmode(!this.game.getOptions().isUhcmode());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(20, randomTeams(), (player, click) -> {
            this.game.getOptions().setRandomTeams(!this.game.getOptions().isRandomTeams());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(21, teamSize(), (player, click) -> {
            switch (this.game.getOptions().getTeamSize()) {
                case 1 -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setTeamSize(2);
                    } else {
                        this.game.getOptions().setTeamSize(5);
                    }
                }
                case 2 ->{
                    if (click.isLeftClick()) {
                        this.game.getOptions().setTeamSize(3);
                    } else {
                        this.game.getOptions().setTeamSize(1);
                    }
                }
                case 3 -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setTeamSize(4);
                    } else {
                        this.game.getOptions().setTeamSize(2);
                    }
                }
                case 4 -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setTeamSize(5);
                    } else {
                        this.game.getOptions().setTeamSize(3);
                    }
                }
                default -> {
                    if (click.isLeftClick()) {
                        this.game.getOptions().setTeamSize(1);
                    } else {
                        this.game.getOptions().setTeamSize(4);
                    }
                }
            }
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        }) ;

        setItem(22, fullcardItem(), (player, click) -> {
            this.game.getOptions().setFullCard(!this.game.getOptions().isFullCard());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(23, keepInventoryItem(), (player, click) -> {
            this.game.getOptions().setKeepInventory(!this.game.getOptions().isKeepInventory());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });

        setItem(24, removeOnMarkItem(), (player, click) -> {
            this.game.getOptions().setRemoveMarkedItems(!this.game.getOptions().isRemoveMarkedItems());
            player.playSound(player, Sound.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 1.0f, 1.0f);

            onSetItems();
        });


    }

    private ItemStack removeOnMarkItem() {
        ItemStack itemStack = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("delete_items")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isRemoveMarkedItems()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack keepInventoryItem() {
        ItemStack itemStack = new ItemStack(Material.BUNDLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("keep_inventory")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isKeepInventory()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack fullcardItem() {
        ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("full_card")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isFullCard()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack uhcmodeItem() {
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("uhc")));
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isUhcmode()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


    private ItemStack timelimitItem() {
        ItemStack itemStack = new ItemStack(Material.CLOCK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("time_limit")));
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isTimeLimit()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack hardcoreItem() {
        ItemStack itemStack = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("hardcore")));
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isHardcore()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


    private ItemStack tipoBingoItem() {
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("bingo_type")));
        if (this.game.getOptions().getType() != null) {
            BingoType type = this.game.getOptions().getType();
            switch (type) {
                case ALL -> {
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(""));
                    lore.add(TextBuilder.minimessage("<b><white>>> ALL"));
                    lore.add(TextBuilder.minimessage("<gray>HORIZONTAL"));
                    lore.add(TextBuilder.minimessage("<gray>VERTICAL"));
                    lore.add(TextBuilder.minimessage("<gray>DIAGONAL"));
                    itemMeta.lore(lore);
                }
                case HORIZONTAL -> {
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(""));
                    lore.add(TextBuilder.minimessage("<gray>ALL"));
                    lore.add(TextBuilder.minimessage("<b><white>>> HORIZONTAL"));
                    lore.add(TextBuilder.minimessage("<gray>VERTICAL"));
                    lore.add(TextBuilder.minimessage("<gray>DIAGONAL"));
                    itemMeta.lore(lore);
                }
                case VERTICAL -> {
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(""));
                    lore.add(TextBuilder.minimessage("<gray>ALL"));
                    lore.add(TextBuilder.minimessage("<gray>HORIZONTAL"));
                    lore.add(TextBuilder.minimessage("<b><white>>> VERTICAL"));
                    lore.add(TextBuilder.minimessage("<gray>DIAGONAL"));
                    itemMeta.lore(lore);
                }
                case DIAGONAL -> {
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(""));
                    lore.add(TextBuilder.minimessage("<gray>ALL"));
                    lore.add(TextBuilder.minimessage("<gray>HORIZONTAL"));
                    lore.add(TextBuilder.minimessage("<gray>VERTICAL"));
                    lore.add(TextBuilder.minimessage("<b><white>>> DIAGONAL"));
                    itemMeta.lore(lore);
                }
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack pvpItem() {
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("pvp")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isPvp()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack displayTimeItem() {
        ItemStack itemStack = new ItemStack(Material.REDSTONE_LAMP);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("display_time")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isDisplayTime()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private ItemStack maxTimeItem() {
        ItemStack itemStack = new ItemStack(Material.REPEATER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("max_time")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();

        int totalSeconds = this.game.getOptions().getMaxTime();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;

        lore.add(TextBuilder.minimessage("<gray>" + String.format("%02d", hours) + "h" + String.format("%02d", minutes) + "min"));
        lore.add(Component.text(""));
        lore.add(TextBuilder.minimessage(lang.ui("click_to_open")));
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack randomTeams() {
        ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_BANNER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("random_teams")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        if (this.game.getOptions().isRandomTeams()) {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<white><b>>> <green>ON"));
            lore.add(TextBuilder.minimessage("<gray>OFF"));
        } else {
            lore.add(Component.text(""));
            lore.add(TextBuilder.minimessage("<gray>ON"));
            lore.add(TextBuilder.minimessage("<white><b>>> <red>OFF"));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack teamSize() {
        ItemStack itemStack = new ItemStack(Material.ENDER_EYE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(TextBuilder.minimessage(lang.ui("team_size")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        List<Component> lore = new ArrayList<>();
        switch (this.game.getOptions().getTeamSize()) {
            case 1 -> {
                lore.add(Component.text(""));
                lore.add(TextBuilder.minimessage("<white><b>>> 1"));
                lore.add(TextBuilder.minimessage("<gray>2"));
                lore.add(TextBuilder.minimessage("<gray>3"));
                lore.add(TextBuilder.minimessage("<gray>4"));
                lore.add(TextBuilder.minimessage("<gray>5"));
            }
            case 2 -> {
                lore.add(Component.text(""));
                lore.add(TextBuilder.minimessage("<gray>1"));
                lore.add(TextBuilder.minimessage("<white><b>>> 2"));
                lore.add(TextBuilder.minimessage("<gray>3"));
                lore.add(TextBuilder.minimessage("<gray>4"));
                lore.add(TextBuilder.minimessage("<gray>5"));
            }
            case 3 -> {
                lore.add(Component.text(""));
                lore.add(TextBuilder.minimessage("<gray>1"));
                lore.add(TextBuilder.minimessage("<gray>2"));
                lore.add(TextBuilder.minimessage("<white><b>>> 3"));
                lore.add(TextBuilder.minimessage("<gray>4"));
                lore.add(TextBuilder.minimessage("<gray>5"));
            }
            case 4 -> {
                lore.add(Component.text(""));
                lore.add(TextBuilder.minimessage("<gray>1"));
                lore.add(TextBuilder.minimessage("<gray>2"));
                lore.add(TextBuilder.minimessage("<gray>3"));
                lore.add(TextBuilder.minimessage("<white><b>>> 4"));
                lore.add(TextBuilder.minimessage("<gray>5"));
            }
            case 5 -> {
                lore.add(Component.text(""));
                lore.add(TextBuilder.minimessage("<gray>1"));
                lore.add(TextBuilder.minimessage("<gray>2"));
                lore.add(TextBuilder.minimessage("<gray>3"));
                lore.add(TextBuilder.minimessage("<gray>4"));
                lore.add(TextBuilder.minimessage("<white><b>>> 5"));
            }
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

    @Override
    public void open(Player player) {
        super.open(player);
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 2.0f);
    }
}
