package codegen.program;

import codegen.Codes.ByteCodes;

public class Code {
  private ByteCodes code;

  public Code(ByteCodes code) {
    this.code = code;
  }

  public ByteCodes getByteCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return this.code.toString();
  }
}
