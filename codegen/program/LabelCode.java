package codegen.program;

import codegen.Codes.ByteCodes;

public class LabelCode extends Code {
  private String label;

  public LabelCode(ByteCodes code, String label) {
    super(code);

    this.label = label;
  }

  @Override
  public String toString() {
    return String.format("%s %s", super.toString(), label);
  }
}
