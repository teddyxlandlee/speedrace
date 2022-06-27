package xland.mcplugin.speedrace;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public final class Speedrace extends JavaPlugin {
    @Nullable
    private SpeedraceGame game;
    private SpeedraceGame.Settings settings = new SpeedraceGame.Settings();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getPluginLoader().createRegisteredListeners(new SpeedraceListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (!"speedrace".equalsIgnoreCase(command.getName())) return false;
        if (args.length == 0) return false;
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "start":
                if (settings.canStart()) {
                    game = settings.toGame();
                    Bukkit.broadcastMessage("Game starts!");
                } else {
                    sender.sendMessage("Game can't start!");
                }
                return true;
            case "stop":
                finish();
                return true;
            case "target":
                SpeedraceGame.setPredicate(settings, args);
                sender.sendMessage("OK");
                return true;
            case "players":
                List<Player> players = SpeedraceGame.setPlayers(settings, args);
                Bukkit.broadcastMessage("===================\n" +
                        "Speedrace Players:");
                players.stream().map(Player::getDisplayName).forEachOrdered(Bukkit::broadcastMessage);
                Bukkit.broadcastMessage("===================");
                return true;
            default:
                return false;
        }
    }

    @Nullable
    public SpeedraceGame getGame() {
        return game;
    }

    public void finish() {
        game = null;
        Bukkit.broadcastMessage("Game finishes!");
    }
}
