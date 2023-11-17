package config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VisitorConfiguration {
  public static final String VISITOR_PACKAGE = "visitor";
  public static final String VISITOR_BASE_CLASS = "TreeVisitor";
  public static final String PRINT_VISITOR_CLASS = "PrintVisitor";
  public static final String TEST_VISITOR_CLASS = "TestVisitor";

  public static final Path VISITOR_PACKAGE_PATH = Paths.get(VISITOR_PACKAGE);
  public static final Path TREE_VISITOR_PATH = Paths.get(
      VISITOR_PACKAGE, String.format("%s.java", VISITOR_BASE_CLASS));
  public static final Path PRINT_VISITOR_PATH = Paths.get(
      VISITOR_PACKAGE, String.format("%s.java", PRINT_VISITOR_CLASS));
  public static final Path TEST_VISITOR_PATH = Paths.get(
      "tests", "helpers", VISITOR_PACKAGE, String.format("%s.java", TEST_VISITOR_CLASS));
}
