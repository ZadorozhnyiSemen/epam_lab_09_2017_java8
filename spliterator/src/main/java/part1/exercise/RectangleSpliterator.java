package part1.exercise;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int[][] array;
    private final long endExclusive;
    private long startInclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, checkArrayAndCalcEstimatedSize(array));
    }

    private RectangleSpliterator(int[][] array, long startInclusive, long endExclusive) {
        super(endExclusive - startInclusive,
                Spliterator.IMMUTABLE
                        | Spliterator.ORDERED
                        | Spliterator.SIZED
                        | Spliterator.SUBSIZED
                        | Spliterator.NONNULL);
        this.array = array;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    private static long checkArrayAndCalcEstimatedSize(int[][] array) {
        if (!Arrays.stream(array).allMatch(ar -> ar.length == array[0].length)) {
            throw new IllegalStateException("Array is not recrangular");
        }
        return array.length * array[0].length;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive < endExclusive) {
            int value = array[(int) (startInclusive / array[0].length)][(int) (startInclusive % array[0].length)];
            ++startInclusive;
            action.accept(value);
            return true;
        }
        return false;
    }

    @Override
    public OfInt trySplit() {
        long len = endExclusive - startInclusive;
        if (len < 2) {
            return null;
        }
        long mid = startInclusive + len / 2;
        RectangleSpliterator result = new RectangleSpliterator(array, startInclusive, mid);
        startInclusive = mid;
        return result;
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }
}
