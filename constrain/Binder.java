package constrain;

import lexer.daos.Symbol;

public class Binder {
  private Object value;
  private Symbol previousTop;
  private Binder tail;

  protected Binder(Object value, Symbol previousTop, Binder tail) {
    this.value = value;
    this.previousTop = previousTop;
    this.tail = tail;
  }

  protected Object getValue() {
    return this.value;
  }

  protected Symbol getPreviousTop() {
    return this.previousTop;
  }

  protected Binder getTail() {
    return this.tail;
  }
}
