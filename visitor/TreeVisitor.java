package visitor;

import ast.AST;
import ast.trees.*;

public abstract class TreeVisitor {

  public void visitChildren(AST node) throws Exception {
    for (AST child: node.getChildren()) {
      child.accept(this);
    }
  }

  public abstract Object visit(ProgramTree node) throws Exception;

  public abstract Object visit(BlockTree node) throws Exception;

  public abstract Object visit(DeclarationTree node) throws Exception;

  public abstract Object visit(FunctionDeclarationTree node) throws Exception;

  public abstract Object visit(FormalsTree node) throws Exception;

  public abstract Object visit(IntTypeTree node) throws Exception;

  public abstract Object visit(BoolTypeTree node) throws Exception;

  public abstract Object visit(IfTree node) throws Exception;

  public abstract Object visit(WhileTree node) throws Exception;

  public abstract Object visit(ReturnTree node) throws Exception;

  public abstract Object visit(AssignmentTree node) throws Exception;

  public abstract Object visit(CallTree node) throws Exception;

  public abstract Object visit(ActualArgumentsTree node) throws Exception;

  public abstract Object visit(RelOpTree node) throws Exception;

  public abstract Object visit(AddOpTree node) throws Exception;

  public abstract Object visit(MultOpTree node) throws Exception;

  public abstract Object visit(IntTree node) throws Exception;

  public abstract Object visit(IdentifierTree node) throws Exception;

  public abstract Object visit(IterationTree node) throws Exception;

  public abstract Object visit(RangeTree node) throws Exception;

}