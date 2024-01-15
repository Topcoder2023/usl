package com.gitee.usl.grammar.runtime.type.seq;

import java.util.Iterator;

import com.gitee.usl.grammar.runtime.type.Collector;
import com.gitee.usl.grammar.runtime.type.Sequence;
import com.googlecode.aviator.exception.ExpressionRuntimeException;

public class LimitedSequence<T> extends AbstractSequence<T> {

  private final Sequence<T> seq;
  private final int maxLoopCount;


  public LimitedSequence(final Sequence<T> seq, final int maxLoopCount) {
    super();
    this.seq = seq;
    this.maxLoopCount = maxLoopCount;
  }

  @Override
  public Iterator<T> iterator() {
    final Iterator<T> rawIt = this.seq.iterator();
    return new Iterator<T>() {
      int c = 0;

      @Override
      public boolean hasNext() {
        return rawIt.hasNext();
      }

      @Override
      public T next() {
        if (++this.c > LimitedSequence.this.maxLoopCount) {
          throw new ExpressionRuntimeException(
              "Overflow max loop count: " + LimitedSequence.this.maxLoopCount);
        }
        return rawIt.next();
      }

      @Override
      public void remove() {
        rawIt.remove();
      }

    };
  }

  @Override
  public Collector newCollector(final int size) {
    return this.seq.newCollector(size);
  }

  @Override
  public int hintSize() {
    return this.seq.hintSize();
  }

}
