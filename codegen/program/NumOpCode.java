package codegen.program;

import codegen.Codes.ByteCodes;

public class NumOpCode extends Code {
  private int n;

  public NumOpCode(ByteCodes code, int n) {
    super(code);

    this.n = n;
  }

  public int getN() {
    return this.n;
  }

  @Override
  public String toString() {
    return String.format("%s %d", super.toString(), this.n);
  }

}
