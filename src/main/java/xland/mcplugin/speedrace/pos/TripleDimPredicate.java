package xland.mcplugin.speedrace.pos;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

public final class TripleDimPredicate implements Predicate<Location> {
    private final IntPredicate xp, yp, zp;
    private final NamespacedKey dimension;

    public TripleDimPredicate(IntPredicate xp, IntPredicate yp, IntPredicate zp, String dimension) {
        this.xp = xp;
        this.yp = yp;
        this.zp = zp;
        this.dimension = NamespacedKey.fromString(dimension);
    }

    public TripleDimPredicate(IntPredicate xp, IntPredicate yp, IntPredicate zp) {
        this(xp, yp, zp, "minecraft:overworld");
    }

    public static TripleDimPredicate fromString(String[] args, int offset) {
        Preconditions.checkArgument(args.length >= offset+2);
        if (args.length == offset+2) {
            // no y present
            IntPredicate xp = OneDimPredicates.ofInt(args[offset]),
                    zp = OneDimPredicates.ofInt(args[offset+1]);
            return new TripleDimPredicate(xp, d->true, zp);
        }
        String dimension = "minecraft:overworld";
        if (args.length >= offset+4) {
            // dimension
            dimension = args[offset+3];
        }
        IntPredicate xp = OneDimPredicates.ofInt(args[offset]),
                yp = OneDimPredicates.ofInt(args[offset+1]),
                zp = OneDimPredicates.ofInt(args[offset+2]);
        return new TripleDimPredicate(xp, yp, zp, dimension);
    }


    @Override
    public boolean test(Location location) {
        World world = location.getWorld();
        return  world != null && world.getKey().equals(dimension) &&
                xp.test(location.getBlockX()) &&
                yp.test(location.getBlockY()) &&
                zp.test(location.getBlockZ());
    }
}
