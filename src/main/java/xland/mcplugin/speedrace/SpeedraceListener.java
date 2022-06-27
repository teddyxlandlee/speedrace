package xland.mcplugin.speedrace;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedraceListener implements Listener {
    private final Speedrace plugin;

    public SpeedraceListener(Speedrace plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        SpeedraceGame game = plugin.getGame();
        if (game != null) {
            Player player = event.getPlayer();
            if (game.checkOne(player)) {
                game.markOne(player);
                Bukkit.broadcastMessage(player.getDisplayName() + " finished!");
                if (game.canFinish())
                    plugin.finish();
            }
        }
    }
}
