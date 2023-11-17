package visitor;

import ast.*;
import ast.trees.*;

public class PrintVisitor extends TreeVisitor {

  private int indentation = 2;
  private final int INDENT_BY = 2;

  public void print(String nodeDescription, AST node) throws Exception {
    String format = "%2d: %-35.35s";

    if (node.getDecoration() != null) {
      format = format + String.format(
          " %-10s",
          String.format(" Dec: %d", node.getDecoration().getNodeNumber()));
    }

    if (node.getFrameOffset() != null) {
      format = format + String.format(
          " %-15s",
          String.format(" Offset: %d", node.getFrameOffset()));
    }

    if (node.getLabel() != null) {
      format = format + String.format(
          " %-15s",
          String.format(" Label: %s", node.getLabel()));
    }

    System.out.println(
        String.format(format,
            node.getNodeNumber(),
            String.format("%" + indentation + "s%s", "", nodeDescription)));

    indentation += INDENT_BY;
    visitChildren(node);
    indentation -= INDENT_BY;
  }

  @Override
  public Object visit(ProgramTree node) throws Exception {
    print("Program", node);
    return null;
  }

  @Override
  public Object visit(BlockTree node) throws Exception {
    print("Block", node);
    return null;
  }

  @Override
  public Object visit(DeclarationTree node) throws Exception {
    print("Declaration", node);
    return null;
  }

  @Override
  public Object visit(FunctionDeclarationTree node) throws Exception {
    print("FunctionDeclaration", node);
    return null;
  }

  @Override
  public Object visit(FormalsTree node) throws Exception {
    print("Formals", node);
    return null;
  }

  @Override
  public Object visit(IntTypeTree node) throws Exception {
    print("IntType", node);
    return null;
  }

  @Override
  public Object visit(BoolTypeTree node) throws Exception {
    print("BoolType", node);
    return null;
  }

  @Override
  public Object visit(IfTree node) throws Exception {
    print("If", node);
    return null;
  }

  @Override
  public Object visit(WhileTree node) throws Exception {
    print("While", node);
    return null;
  }

  @Override
  public Object visit(ReturnTree node) throws Exception {
    print("Return", node);
    return null;
  }

  @Override
  public Object visit(AssignmentTree node) throws Exception {
    print("Assignment", node);
    return null;
  }

  @Override
  public Object visit(CallTree node) throws Exception {
    print("Call", node);
    return null;
  }

  @Override
  public Object visit(ActualArgumentsTree node) throws Exception {
    print("ActualArguments", node);
    return null;
  }

  @Override
  public Object visit(RelOpTree node) throws Exception {
    print(String.format("RelOp: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
    return null;
  }

  @Override
  public Object visit(AddOpTree node) throws Exception {
    print(String.format("AddOp: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
    return null;
  }

  @Override
  public Object visit(MultOpTree node) throws Exception {
    print(String.format("MultOp: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
    return null;
  }

  @Override
  public Object visit(IntTree node) throws Exception {
    print(String.format("Int: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
    return null;
  }

  @Override
  public Object visit(IdentifierTree node) throws Exception {
    print(String.format("Identifier: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
    return null;
  }

  @Override
  public Object visit(IterationTree node) throws Exception {
    print("Iteration", node);
    return null;
  }

  @Override
  public Object visit(RangeTree node) throws Exception {
    print("Range", node);
    return null;
  }

}