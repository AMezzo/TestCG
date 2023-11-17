package config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ParserConfiguration {
  public static final String AST_PACKAGE = "ast";
  public static final String TREE_PACKAGE = "trees";
  public static final String AST_FILE_NAME = "asts.txt";

  public static final Path AST_FILE_PATH = Paths.get(
      CompilerConfiguration.TOOLS_PACKAGE,
      CompilerConfiguration.DEFINITIONS_PACKAGE,
      AST_FILE_NAME);

  public static final Path TREE_DIRECTORY = Paths.get(AST_PACKAGE, TREE_PACKAGE);

}
