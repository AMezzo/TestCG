package tools.visitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import config.ParserConfiguration;
import tools.FileGeneratorTool;

import static tools.ToolHelpers.*;

public class CreateVisitor extends FileGeneratorTool {
  private String fullyQualifiedPath;
  private String className;
  private String packageString;

  public CreateVisitor(Path specFile, String fullyQualifiedPath) {
    super(specFile);

    this.fullyQualifiedPath = fullyQualifiedPath;

    String[] pathParts = fullyQualifiedPath.split(
        FileSystems.getDefault().getSeparator());

    this.className = pathParts[pathParts.length - 1].replace(".java", "");

    this.packageString = "";
    for (int i = 0; i < pathParts.length - 1; i++) {
      this.packageString = this.packageString + pathParts[i] + ".";
    }
    this.packageString = this.packageString.substring(0, this.packageString.length() - 1);
  }

  @Override
  public void regenerateSourceFile() {
    File visitorFile = new File(this.fullyQualifiedPath);
    Path parent = visitorFile.toPath().getParent();
    try {
      Files.createDirectories(parent);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (FileWriter writer = new FileWriter(visitorFile)) {
      writer.write(
          String.format("package %s;%s", this.packageString, getNewLines(2)));

      writer.write(
          String.join("",
              List.of(
                  "import ast.*;",
                  getNewLines(1),
                  "import ast.trees.*;",
                  getNewLines(1),
                  "import visitor.TreeVisitor;",
                  getNewLines(2))));

      writer.write(
          String.format("public class %s extends TreeVisitor {%s", this.className, getNewLines(1)));

      while (this.hasNext()) {
        String[] parts = this.next().split("\\s+");
        String visitedTreeName = parts[0];

        writer.write(
            String.join("",
                List.of(
                    getNewLines(1),
                    getIndentation(1),
                    "@Override",
                    getNewLines(1),
                    getIndentation(1),
                    String.format("public Object visit(%sTree tree) {", visitedTreeName),
                    getNewLines(1),
                    getIndentation(1),
                    "}",
                    getNewLines(1))));
      }

      writer.write(getNewLines(1));
      writer.write("}");
    } catch (Exception e) {
      failExecution(e);
    }
  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("usage: java tools.visitor.CreateVisitor <fully qualified classname>");
      System.exit(1);
    }

    CreateVisitor create = new CreateVisitor(ParserConfiguration.AST_FILE_PATH, args[0]);
    create.regenerateSourceFile();
  }

}
