package part2.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {
    private final OfDouble inner;
    private int currentIndex;

    public ZipWithIndexDoubleSpliterator(OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        return inner.tryAdvance((double el) -> action.accept(new IndexedDoublePair(currentIndex++, el)));
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        inner.forEachRemaining((double el) -> action.accept(new IndexedDoublePair(currentIndex++, el)));
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        if (inner.hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED)) {
            OfDouble newStream = inner.trySplit();
            if (newStream == null) {
                return null;
            }
            int currPosition = currentIndex;
            currentIndex += newStream.estimateSize();
            return new ZipWithIndexDoubleSpliterator(currPosition, inner.trySplit());
        } else {
            return super.trySplit();
        }
    }

    @Override
    public int characteristics() {
        return inner.characteristics();
    }

    @Override
    public long estimateSize() {
        return inner.estimateSize();
    }
}
