package tools.visitor;

import config.VisitorConfiguration;

import static tools.ToolHelpers.*;

public class TreeVisitorContents {
  public static String HEADER = String.join("",
      String.format("package %s;", VisitorConfiguration.VISITOR_PACKAGE),
      getNewLines(2),
      "import ast.AST;",
      getNewLines(1),
      "import ast.trees.*;",
      getNewLines(2),
      String.format("public abstract class %s {", VisitorConfiguration.VISITOR_BASE_CLASS),
      endAndIndent(2, 1),
      "public void visitChildren(AST node) throws Exception {",
      endAndIndent(1, 2),
      "for (AST child: node.getChildren()) {",
      endAndIndent(1, 3),
      "child.accept(this);",
      endAndIndent(1, 2),
      "}",
      endAndIndent(1, 1),
      "}",
      getNewLines(2));

  public static String fileContents(String className) {
    return String.join("",
        getIndentation(1),
        String.format("public abstract Object visit(%sTree node) throws Exception;", className),
        getNewLines(2));
  }
}
