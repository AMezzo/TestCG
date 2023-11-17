package tests.helpers.visitor;

import ast.*;
import ast.trees.*;
import java.util.List;
import visitor.TreeVisitor;

public class TestVisitor extends TreeVisitor {
  private List<AST> expected;
  private int index;

  public TestVisitor(final List<AST> expected) {
    this.expected = expected;
    this.index = 0;
  }

  public Object test(AST t) throws Exception {
    try {
      expected.get(index).getClass().cast(t);

      index++;
      return testChildren(t);
    } catch (ClassCastException exception) {
      return String.format(
          "Expected [%s] but got [%s]",
          expected.get(index).getClass().getSimpleName(),
          t.getClass().getSimpleName());
    }
  }

  private Object test(AST t, String expectedSymbol, String actualSymbol) throws Exception {
    try {
      expected.get(index).getClass().cast(t);

      if (!expectedSymbol.equals(actualSymbol)) {
        throw new Exception(
            String.format(
                "Expected [%s] but got [%s]",
                expectedSymbol,
                actualSymbol));
      }

      index++;
      return testChildren(t);
    } catch (ClassCastException exception) {
      return String.format(
          "Expected [%s] but got [%s]",
          expected.get(index).getClass().getSimpleName(),
          t.getClass().getSimpleName());
    } catch (Exception exception) {
      return exception.getMessage();
    }
  }

  private Object testChildren(AST t) throws Exception {
    for (AST child : t.getChildren()) {
      Object result = child.accept(this);

      if (result != null) {
        return result;
      }
    }

    return null;
  }

  @Override
  public Object visit(ProgramTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(BlockTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(DeclarationTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(FunctionDeclarationTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(FormalsTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(IntTypeTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(BoolTypeTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(IfTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(WhileTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(ReturnTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(AssignmentTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(CallTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(ActualArgumentsTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(RelOpTree tree) throws Exception {
    String actualSymbol = ((ISymbolTree) tree).getSymbol().toString();
    String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

    return test(tree, expectedSymbol, actualSymbol);
  }

  @Override
  public Object visit(AddOpTree tree) throws Exception {
    String actualSymbol = ((ISymbolTree) tree).getSymbol().toString();
    String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

    return test(tree, expectedSymbol, actualSymbol);
  }

  @Override
  public Object visit(MultOpTree tree) throws Exception {
    String actualSymbol = ((ISymbolTree) tree).getSymbol().toString();
    String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

    return test(tree, expectedSymbol, actualSymbol);
  }

  @Override
  public Object visit(IntTree tree) throws Exception {
    String actualSymbol = ((ISymbolTree) tree).getSymbol().toString();
    String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

    return test(tree, expectedSymbol, actualSymbol);
  }

  @Override
  public Object visit(IdentifierTree tree) throws Exception {
    String actualSymbol = ((ISymbolTree) tree).getSymbol().toString();
    String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

    return test(tree, expectedSymbol, actualSymbol);
  }

  @Override
  public Object visit(IterationTree tree) throws Exception {
    return test(tree);
  }

  @Override
  public Object visit(RangeTree tree) throws Exception {
    return test(tree);
  }

}