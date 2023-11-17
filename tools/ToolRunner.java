package tools;

import java.io.IOException;
import java.nio.file.Files;

import config.LexerConfiguration;
import config.ParserConfiguration;
import config.VisitorConfiguration;
import tools.ast.TreeGenerator;
import tools.lexer.SymbolTableGenerator;
import tools.lexer.TokenKindGenerator;
import tools.visitor.VisitorGenerator;

public class ToolRunner {
  public static void main(String[] args) {
    try {
      Files.createDirectories(LexerConfiguration.TOKEN_KIND_FILE_PATH.getParent());
      Files.createDirectories(LexerConfiguration.UNIQUE_SYMBOL_MAP_FILE_PATH.getParent());
      Files.createDirectories(ParserConfiguration.TREE_DIRECTORY);
      Files.createDirectories(VisitorConfiguration.VISITOR_PACKAGE_PATH);
      Files.createDirectories(VisitorConfiguration.TEST_VISITOR_PATH.getParent());
    } catch (IOException e) {
      System.err.println("Failed to create directories for generated files.");
      e.printStackTrace();
      System.exit(1);
    }

    FileGeneratorTool tokenKindGenerator =
        new TokenKindGenerator(LexerConfiguration.TOKEN_FILE_PATH);
    tokenKindGenerator.regenerateSourceFile();

    FileGeneratorTool symbolTableGenerator =
        new SymbolTableGenerator(LexerConfiguration.TOKEN_FILE_PATH);
    symbolTableGenerator.regenerateSourceFile();

    FileGeneratorTool treeGenerator = new TreeGenerator(ParserConfiguration.AST_FILE_PATH);
    treeGenerator.regenerateSourceFile();

    FileGeneratorTool visitorGenerator =
        new VisitorGenerator(ParserConfiguration.AST_FILE_PATH);
    visitorGenerator.regenerateSourceFile();
  }
}
