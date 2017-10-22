package part1.exercise;

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
        // TODO

        return array.length * array[0].length;
    }

    @Override
    public OfInt trySplit() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        // TODO
        throw new UnsupportedOperationException();
    }


}
