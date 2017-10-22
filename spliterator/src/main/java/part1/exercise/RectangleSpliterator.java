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

/*    public RectangleSpliterator(int[][] array) {
        super(checkArrayAndCalcEstimatedSize(array), 0);
//       super(estimatedSize, Spliterator.IMMUTABLE
//                          | Spliterator.ORDERED
//                          | Spliterator.SIZED
//                          | Spliterator.SUBSIZED
//                          | Spliterator.NONNULL);
        this.array = array;
    }*/

    public RectangleSpliterator(int[][] array, long startInclusive, long endExclusive) {
        super(endExclusive - startInclusive,
                Spliterator.IMMUTABLE |
                        Spliterator.ORDERED |
                        Spliterator.SIZED |
                        Spliterator.SUBSIZED |
                        Spliterator.NONNULL);
        this.array = array;
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
    }

    private static long checkArrayAndCalcEstimatedSize(int[][] array) {
        if (Arrays.stream(array).anyMatch(ints -> ints.length != array[0].length)) {
            throw new RuntimeException("Array not rectangle");
        }
        return array.length * array[0].length;
    }

    @Override
    public OfInt trySplit() {
        long length = endExclusive - startInclusive;
        long midPoint = startInclusive + length/2;

        if (length < 2) {
            return null;
        }

        RectangleSpliterator result = new RectangleSpliterator(array, startInclusive, midPoint);
        startInclusive = midPoint;
        return result;
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInclusive < endExclusive) {
            int value = array[convertToInt(startInclusive / array[0].length)][convertToInt(startInclusive % array[0].length)];
            startInclusive += 1;
            action.accept(value);
            return true;
        }
        return false;
    }

    private int convertToInt (long toConvert) {
        return Math.toIntExact(toConvert);
    }
}
