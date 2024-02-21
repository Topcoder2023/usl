package com.gitee.usl.grammar.runtime.type.seq;

import com.gitee.usl.grammar.runtime.type.Collector;

import java.util.Iterator;

/**
 * Sequence for CharSequence.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class CharSeqSequence extends AbstractSequence<String> {
  private final CharSequence cs;


  public CharSeqSequence(final CharSequence cs) {
    super();
    this.cs = cs;
  }

  @Override
  public int hintSize() {
    return this.cs.length();
  }



  @Override
  public Collector newCollector(final int size) {
    return new ListCollector(size, false);
  }



  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        return this.i < CharSeqSequence.this.cs.length();
      }

      @Override
      public String next() {
        return String.valueOf(CharSeqSequence.this.cs.charAt(this.i++));
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

}
