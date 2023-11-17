package tools.visitor;

import static tools.ToolHelpers.*;
import config.VisitorConfiguration;

public class TestVisitorContents {

  public static final String HEADER = String.join("",
      "package tests.helpers.visitor;", getNewLines(2),
      "import ast.*;", getNewLines(1),
      "import ast.trees.*;", getNewLines(1),
      "import java.util.List;", getNewLines(1),
      String.format("import visitor.%s;", VisitorConfiguration.VISITOR_BASE_CLASS),
      getNewLines(2),
      String.format("public class TestVisitor extends %s {",
          VisitorConfiguration.VISITOR_BASE_CLASS),
      endAndIndent(1, 1),
      "private List<AST> expected;", endAndIndent(1, 1),
      "private int index;", endAndIndent(2, 1),
      "public TestVisitor(final List<AST> expected) {", endAndIndent(1, 2),
      "this.expected = expected;", endAndIndent(1, 2),
      "this.index = 0;", endAndIndent(1, 1),
      "}", endAndIndent(2, 1),
      "public Object test(AST t) throws Exception {", endAndIndent(1, 2),
      "try {", endAndIndent(1, 3),
      "expected.get(index).getClass().cast(t);", endAndIndent(2, 3),
      "index++;", endAndIndent(1, 3),
      "return testChildren(t);", endAndIndent(1, 2),
      "} catch (ClassCastException exception) {", endAndIndent(1, 3),
      "return String.format(", endAndIndent(1, 5),
      "\"Expected [%s] but got [%s]\",", endAndIndent(1, 5),
      "expected.get(index).getClass().getSimpleName(),", endAndIndent(1, 5),
      "t.getClass().getSimpleName());", endAndIndent(1, 2),
      "}", endAndIndent(1, 1),
      "}", endAndIndent(2, 1),
      "private Object test(AST t, String expectedSymbol, String actualSymbol) throws Exception {",
      endAndIndent(1, 2),
      "try {", endAndIndent(1, 3),
      "expected.get(index).getClass().cast(t);", endAndIndent(2, 3),
      "if (!expectedSymbol.equals(actualSymbol)) {", endAndIndent(1, 4),
      "throw new Exception(", endAndIndent(1, 6),
      "String.format(", endAndIndent(1, 8),
      "\"Expected [%s] but got [%s]\",", endAndIndent(1, 8),
      "expectedSymbol,", endAndIndent(1, 8),
      "actualSymbol));", endAndIndent(1, 3),
      "}", endAndIndent(2, 3),
      "index++;", endAndIndent(1, 3),
      "return testChildren(t);", endAndIndent(1, 2),
      "} catch (ClassCastException exception) {", endAndIndent(1, 3),
      "return String.format(", endAndIndent(1, 5),
      "\"Expected [%s] but got [%s]\",", endAndIndent(1, 5),
      "expected.get(index).getClass().getSimpleName(),", endAndIndent(1, 5),
      "t.getClass().getSimpleName());", endAndIndent(1, 2),
      "} catch (Exception exception) {", endAndIndent(1, 3),
      "return exception.getMessage();", endAndIndent(1, 2),
      "}", endAndIndent(1, 1),
      "}", endAndIndent(2, 1),
      "private Object testChildren(AST t) throws Exception {", endAndIndent(1, 2),
      "for (AST child : t.getChildren()) {", endAndIndent(1, 3),
      "Object result = child.accept(this);", endAndIndent(2, 3),
      "if (result != null) {", endAndIndent(1, 4),
      "return result;", endAndIndent(1, 3),
      "}", endAndIndent(1, 2),
      "}", endAndIndent(2, 2),
      "return null;", endAndIndent(1, 1),
      "}", getNewLines(2));

  public static String fileContents(String className, boolean isSymbolTree) {
    StringBuffer buffer = new StringBuffer();

    buffer.append(String.join("",
        getIndentation(1),
        "@Override", endAndIndent(1, 1),
        String.format("public Object visit(%sTree tree) throws Exception {", className),
        endAndIndent(1, 2)));

    if (isSymbolTree) {
      buffer.append(String.join("",
          "String actualSymbol = ((ISymbolTree) tree).getSymbol().toString();",
          endAndIndent(1, 2),
          "String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();",
          endAndIndent(2, 2),
          "return test(tree, expectedSymbol, actualSymbol);", endAndIndent(1, 1)));
    } else {
      buffer.append(String.join("",
          "return test(tree);", endAndIndent(1, 1)));
    }

    buffer.append(String.join("", "}", getNewLines(2)));

    return buffer.toString();
  }
}
