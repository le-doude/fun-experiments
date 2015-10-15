package org.ledoude;

import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public class LongRange extends Spliterators.AbstractLongSpliterator {

    final Long start;
    final Long end;
    Long current;

    public LongRange(Long start, Long end) {
        super(end - start, DISTINCT);
        this.start = start;
        this.end = end;
        this.current = start;
    }


    @Override
    public OfLong trySplit() {
        long diff = end - current;
        if (diff > 2) {
            long middle = current + (diff / 2);
            LongRange split = new LongRange(current, middle);
            this.current = middle + 1;
            return split;
        } else {
            return null;
        }
    }

    @Override
    public boolean tryAdvance(LongConsumer action) {
        if (action == null) throw new NullPointerException();
        if (current >= start && current < end) {
            action.accept(current++);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super Long> action) {
        if (action == null) throw new NullPointerException();
        if (current >= start && current < end) {
            action.accept(current++);
            return true;
        } else {
            return false;
        }
    }

}
