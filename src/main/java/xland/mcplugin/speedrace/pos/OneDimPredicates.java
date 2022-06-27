package xland.mcplugin.speedrace.pos;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class OneDimPredicates {
    public static DoublePredicate ofDouble(String spec) {
        return d -> fromSpec(spec, Double::valueOf).test(d);
    }

    public static IntPredicate ofInt(String spec) {
        return i -> fromSpec(spec, Integer::valueOf).test(i);
    }

    @SuppressWarnings("guava")
    protected static <C extends Comparable<C>> Predicate<C> fromSpec(
            String spec, Function<String, C> gen) {
        String[] split = spec.split(",");
        List<com.google.common.base.Predicate<? super C>> l = new ArrayList<>(split.length);
        for (String s : split) {
            if (FULL_CARDS.contains(s)) return d -> true;
            if (s.startsWith(">="))
                l.add(Range.atLeast(gen.apply(s.substring(2))));
            else if (s.startsWith("<="))
                l.add(Range.atMost(gen.apply(s.substring(2))));
            else if (s.startsWith(">"))
                l.add(Range.greaterThan(gen.apply(s.substring(1))));
            else if (s.startsWith("<"))
                l.add(Range.lessThan(gen.apply(s.substring(1))));
            else
                l.add(Range.singleton(gen.apply(s)));
        }
        return Predicates.and(l);
    }

    private static final ImmutableSet<String> FULL_CARDS =
            ImmutableSet.of("*", "-", ".");
}
