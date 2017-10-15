package part1.example;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GenerateStreams {
    public static void main(String[] args) {
        int[] data = new int[100];
        Arrays.fill(data, 1);
        // способы создания стримов
        Stream<Integer> stream1 = Arrays.asList(1, 2, 3).stream();
        Stream<Boolean> booleanStream = Stream.of(false, false, true);
        // Files.line
        String string = "abcde";
        string.chars();
        Stream.generate(() -> 1);
        Stream.iterate(0, integer -> integer + 2); // четные числа
        IntStream ints = new Random().ints(20, 0, 100);
        IntStream stream = Arrays.stream(data);
    }
}
