package constrain;

import ast.AST;

public class ConstraintException extends Exception {

  private ConstrainerErrors error;
  private AST tree;

  public ConstraintException(ConstrainerErrors error, AST tree) {
    this.error = error;
    this.tree = tree;
  }

  @Override
  public String getMessage() {
    return String.format(
        "The Constrainer encountered an error constraining node [%d]: %s",
        this.tree.getNodeNumber(),
        this.error);
  }
}
