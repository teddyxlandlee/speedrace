package xland.mcplugin.speedrace;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import xland.mcplugin.speedrace.pos.TripleDimPredicate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpeedraceGame {
    private final Map<Player, Boolean> players;
    private final TripleDimPredicate predicate;

    public SpeedraceGame(Map<Player, Boolean> players, TripleDimPredicate predicate) {
        this.players = players;
        this.predicate = predicate;
    }

    public SpeedraceGame(Collection<? extends Player> players, TripleDimPredicate predicate) {
        this(players.stream().collect(Collectors.toMap(
                Function.identity(), e->Boolean.FALSE)), predicate);
    }

    public Map<Player, Boolean> getPlayers() {
        return Collections.unmodifiableMap(players);
    }

    public boolean checkOne(Player player) {
        if (!players.containsKey(player) || !player.isOnline()
            || player.getGameMode() == GameMode.SPECTATOR) return false;
        return predicate.test(player.getLocation());
    }

    public void markOne(Player player) {
        if (players.containsKey(player))
            players.put(player, Boolean.TRUE);
    }

    public boolean canFinish() {
        return players.values().stream().allMatch(Boolean.TRUE::equals);
    }

    public static List<Player> setPlayers(Settings settings, String[] args) {
        //Bukkit.getPlayer()// online
        List<Player> players1 = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            Player p = Bukkit.getPlayer(args[i]);
            if (p != null) players1.add(p);
        }
        settings.setPlayers(players1);
        return Collections.unmodifiableList(players1);
    }

    public static void setPredicate(Settings settings, String[] args) {
        TripleDimPredicate tdp = TripleDimPredicate.fromString(args, 1);
        settings.setPredicate(tdp);
    }

    public static class Settings {
        private List<Player> players;
        private TripleDimPredicate predicate;

        public Settings(List<Player> players, TripleDimPredicate predicate) {
            this.players = players;
            this.predicate = predicate;
        }

        public Settings() {}

        public boolean canStart() {
            return players != null && !players.isEmpty() && predicate != null;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public void setPlayers(List<Player> players) {
            this.players = players;
        }

        public TripleDimPredicate getPredicate() {
            return predicate;
        }

        public void setPredicate(TripleDimPredicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Settings settings = (Settings) o;
            return Objects.equals(players, settings.players) && Objects.equals(predicate, settings.predicate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(players, predicate);
        }

        @Override
        public String toString() {
            return "Settings{" +
                    "players=" + players +
                    ", predicate=" + predicate +
                    '}';
        }

        public SpeedraceGame toGame() {
            return new SpeedraceGame(players, predicate);
        }
    }
}
