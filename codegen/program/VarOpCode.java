package codegen.program;

import codegen.Codes.ByteCodes;

public class VarOpCode extends Code {
  private String id;
  private int n;

  public VarOpCode(ByteCodes code, int n, String id) {
    super(code);

    this.n = n;
    this.id = id;
  }

  @Override
  public String toString() {
    return String.format("%s %d %s", super.toString(), this.n, this.id);
  }
}
