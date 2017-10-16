package part1.example;


import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CollectorsExample {
    public static void main(String[] args) {
        IntStream range = IntStream.range(0, 100);
        // "0" -> 0
        // "1" -> 1
        // ...
        // "99" -> 99
        Map<String, Integer> collect1 = range.boxed().collect(Collectors.toMap(String::valueOf, Integer::valueOf));
        Map<String, Integer> collect2 = Stream.of(1, 2, 3, 3, 5).collect(Collectors.toMap(String::valueOf, Integer::valueOf, (old, ne) -> old));
        Map<String, Integer> collect3 = Stream.of(1, 2, 3, 3, 5).collect(Collectors.toMap(String::valueOf, Integer::valueOf, (old, ne) -> old + ne));
        Map<String, Integer> collect4 = Stream.of(1, 2, 3, 3, 5).collect(Collectors.toMap(String::valueOf, Integer::valueOf, (old, ne) -> old + ne, TreeMap::new));
        List<Integer> collect5 = range.boxed().collect(Collectors.toList());
        Map<Boolean, List<Integer>> collect6 = range.boxed().collect(Collectors.groupingBy(intValue -> intValue > 50));
        Map<Boolean, List<Integer>> collect7 = range.boxed().collect(Collectors.partitioningBy(intValue -> intValue > 50)); // partitioningBy - маппинг в булеан
        Integer collect8 = range.boxed().collect(Collectors.summingInt(v -> v));
        IntSummaryStatistics intSummaryStatistics = range.boxed().collect(Collectors.summarizingInt(v -> v));
        String collect9 = Stream.of("a","b","c").collect(Collectors.joining()); // можно склеивать стримы стрингов
        String collect10 = Stream.of("a","b","c").collect(Collectors.joining(" ")); // можно склеивать стримы стрингов
        String collect11 = Stream.of("a","b","c").collect(Collectors.joining(" ", "->", "<-")); // можно склеивать стримы стрингов
    }
}
