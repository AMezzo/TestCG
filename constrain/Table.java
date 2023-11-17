package constrain;

import java.util.HashMap;
import java.util.Map;

import lexer.daos.Symbol;

public class Table {

  private Map<Symbol, Binder> symbols;
  private Symbol top;
  private Binder marks;

  public Table() {
    this.symbols = new HashMap<>();
  }

  public Object get(Symbol key) {
    return this.symbols.get(key).getValue();
  }

  public void put(Symbol key, Object value) {
    this.symbols.put(key, new Binder(value, top, this.symbols.get(key)));
    this.top = key;
  }

  public void beginScope() {
    this.marks = new Binder(null, top, this.marks);
    this.top = null;
  }

  public void endScope() {
    while (this.top != null) {
      Binder topSymbol = this.symbols.get(this.top);

      if (topSymbol.getTail() != null) {
        this.symbols.put(this.top, topSymbol.getTail());
      } else {
        this.symbols.remove(this.top);
      }

      this.top = topSymbol.getPreviousTop();
    }

    this.top = this.marks.getPreviousTop();
    this.marks = this.marks.getTail();
  }

}
