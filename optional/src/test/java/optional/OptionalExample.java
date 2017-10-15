package optional;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"Convert2MethodRef", "ExcessiveLambdaUsage", "ResultOfMethodCallIgnored", "OptionalIsPresent"})
public class OptionalExample {

    @Test
    public void get() {
        Optional<String> o1 = Optional.empty();
        o1.ifPresent(s -> System.out.println(s));
        o1.orElse("t");
        o1.orElseGet(() -> "t");
        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        Optional<String> o1 = getOptional();
        o1.ifPresent(System.out::println);
        if (o1.isPresent()) {
            System.out.println(o1.get());
        }
    }

    @Test
    public void map() {
        Optional<String> o1 = getOptional();
        Function<String, Integer> getLength = String::length;
        Optional<Integer> expected = o1.map(getLength);
        Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.ofNullable(getLength.apply(o1.get()));
        } else {
            actual = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        Optional<String> o1 = getOptional();
        Function<String, Optional<List<Character>>> mapper = str -> {
            char[] chrs = str.toCharArray();
            Optional<List<Character>> chars = Optional.of(new ArrayList<Character>());
            for (char ch : chrs) {
                chars.get().add(ch);
            }
            return chars;
        };
        Optional<List<Character>> expected = o1.flatMap(mapper);
        Optional<List<Character>> actual;
        if (o1.isPresent()) {
            actual = mapper.apply(o1.get());
        } else {
            actual = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        Optional<String> o1 = getOptional();
        Predicate<String> predicate = str -> str.contains("a");
        Optional<String> expected = o1.filter(predicate);
        Optional<String> actual;
        if (o1.isPresent()) {
            actual = predicate.test(o1.get()) ? o1 : Optional.empty();
        } else {
            actual = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean() ? Optional.empty() : Optional.of("abc");
    }
}
