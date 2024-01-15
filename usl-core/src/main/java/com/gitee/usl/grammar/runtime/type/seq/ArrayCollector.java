package com.gitee.usl.grammar.runtime.type.seq;

import com.gitee.usl.grammar.runtime.type.Collector;

public class ArrayCollector implements Collector {
  Object[] array;
  int i = 0;

  public ArrayCollector(final int size) {
    this.array = new Object[size];
  }

  @Override
  public void add(final Object e) {
    this.array[this.i++] = e;
  }

  @Override
  public Object getRawContainer() {
    return this.array;
  }
}
