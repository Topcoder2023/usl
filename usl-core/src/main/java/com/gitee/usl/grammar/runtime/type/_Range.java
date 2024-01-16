package com.gitee.usl.grammar.runtime.type;

import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.gitee.usl.grammar.utils.ArrayUtils;
import com.gitee.usl.grammar.utils.Env;

import java.lang.reflect.Array;
import java.util.*;

/**
 * A range in [start, end) with step.
 *
 * @author dennis(killme2008@gmail.com)
 * @since 5.0.0
 */
public final class _Range extends _Object implements Sequence<Number> {


  private static final long serialVersionUID = 1463899968843425932L;
  private static final _Long ZERO = _Long.valueOf(0L);
  public static final _Range LOOP = new _Range(ZERO, ZERO, ZERO);

  static {
    LOOP.isLoop = true;
  }

  private final _Number step;
  private final _Number start;
  private final _Number end;
  final boolean forward;
  private boolean isLoop;

  public boolean isLoop() {
    return this.isLoop;
  }

  public _Range(final _Number start, final _Number end, final _Number step) {
    super();
    this.start = start;
    this.end = end;
    this.step = step;
    this.forward = _Range.this.step.compare(ZERO, Env.EMPTY_ENV) >= 0;
    this.isLoop = false;
  }

  @Override
  public String desc(final Map<String, Object> env) {
    return "<Range, [" + this.start.getValue(env) + ", " + this.end.getValue(env) + "], "
        + this.step.getValue(env) + ">";
  }


  @Override
  public int innerCompare(final _Object other, final Map<String, Object> env) {
    throw new CompareNotSupportedException();
  }



  @Override
  public _Type getAviatorType() {
    return _Type.Range;
  }



  @Override
  public Object getValue(final Map<String, Object> env) {
    return this;
  }



  public _Number first() {
    return this.start;
  }

  public _Number last() {
    return this.end;
  }


  @Override
  public int hintSize() {
    try {
      return size();
    } catch (Throwable t) {
      return 10;
    }
  }

  public int size() {
    int size =
        ((Number) this.end.sub(this.start, null).div(this.step, null).getValue(null)).intValue();
    return size < 0 ? 0 : size;
  }

  @Override
  public Collector newCollector(final int size) {
    if (size <= 0) {
      return new Collector() {
        List<Object> list = new ArrayList<>();

        @Override
        public void add(final Object e) {
          this.list.add(e);
        }

        @Override
        public Object getRawContainer() {
          return this.list;
        }
      };
    } else {
      return new Collector() {
        Object array = Array.newInstance(Object.class, size);
        int i = 0;

        @Override
        public void add(final Object e) {
          ArrayUtils.set(this.array, this.i++, e);
        }

        @Override
        public Object getRawContainer() {
          return this.array;
        }
      };
    }
  }

  @Override
  public Iterator<Number> iterator() {
    return new Iterator<Number>() {
      _Number current = _Range.this.start;

      @Override
      public boolean hasNext() {
        if (_Range.this.forward) {
          return this.current.compare(_Range.this.end, Env.EMPTY_ENV) < 0;
        } else {
          return this.current.compare(_Range.this.end, Env.EMPTY_ENV) > 0;
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Number next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        _Number result = this.current;
        this.current = (_Number) this.current.add(_Range.this.step, Env.EMPTY_ENV);
        return (Number) result.getValue(Env.EMPTY_ENV);
      }

    };
  }


}
