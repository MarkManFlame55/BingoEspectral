package net.espectralgames.bingoEspectral.utils;

import net.espectralgames.bingoEspectral.BingoEspectral;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;


public class TextBuilder {

    private static final BingoEspectral plugin = BingoEspectral.getPlugin();

    public static Component minimessage(String msg) {
        return MiniMessage.miniMessage().deserialize(msg).decoration(TextDecoration.ITALIC, false);
    }
    public static Component info(String msg) {
        return preffix().append(minimessage(" " + msg));
    }
    public static Component success(String msg) {
        return preffix().append(minimessage("<reset><green> " + msg));
    }
    public static Component error(String msg) {
        return preffix().append(minimessage("<reset><red> " + msg));
    }
    private static Component preffix() {
        YamlConfiguration lang = plugin.getLangConfig();
        return minimessage(lang.getString("bingo.prefix"));
    }
}
