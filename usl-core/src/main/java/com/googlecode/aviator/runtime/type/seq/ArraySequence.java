package com.googlecode.aviator.runtime.type.seq;

import com.googlecode.aviator.runtime.type.Collector;
import com.googlecode.aviator.utils.ArrayUtils;

import java.util.Iterator;

/**
 * Sequence for object array.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class ArraySequence extends AbstractSequence<Object> {
  private final Object a;
  private final int len;



  @Override
  public int hintSize() {
    return this.len;
  }



  @Override
  public Collector newCollector(final int size) {
    if (size <= 0) {
      return new ListCollector(true);
    } else {
      return new ArrayCollector(size);
    }
  }



  public ArraySequence(final Object a) {
    super();
    this.a = a;
    this.len = ArrayUtils.getLength(a);
  }



  @Override
  public Iterator<Object> iterator() {
    return new Iterator<Object>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        return this.i < ArraySequence.this.len;
      }

      @Override
      public Object next() {
        return ArrayUtils.get(ArraySequence.this.a, this.i++);
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

}
