package tools.visitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import config.VisitorConfiguration;
import tools.FileGeneratorTool;
import static tools.ToolHelpers.*;

public class VisitorGenerator extends FileGeneratorTool {
  public VisitorGenerator(Path filePath) {
    super(filePath);
  }

  @Override
  public void regenerateSourceFile() {
    File treeVisitor =
        new File(VisitorConfiguration.TREE_VISITOR_PATH.toAbsolutePath().toString());
    File printVisitor =
        new File(VisitorConfiguration.PRINT_VISITOR_PATH.toAbsolutePath().toString());
    File testVisitor =
        new File(VisitorConfiguration.TEST_VISITOR_PATH.toAbsolutePath().toString());

    try (FileWriter baseClassWriter = new FileWriter(treeVisitor);
        FileWriter printVisitorWriter = new FileWriter(printVisitor);
        FileWriter testVisitoWriter = new FileWriter(testVisitor)) {
      baseClassWriter.write(TreeVisitorContents.HEADER);
      printVisitorWriter.write(PrintVisitorContents.HEADER);
      testVisitoWriter.write(TestVisitorContents.HEADER);

      while (this.hasNext()) {
        String[] parts = this.next().split("\\s+");
        String className = parts[0];
        boolean isSymbolTree = parts.length == 2;

        baseClassWriter.write(TreeVisitorContents.fileContents(className));
        printVisitorWriter
            .write(PrintVisitorContents.fileContents(className, isSymbolTree));
        testVisitoWriter.write(TestVisitorContents.fileContents(className, isSymbolTree));
      }

      baseClassWriter.write("}");
      printVisitorWriter.write("}");
      testVisitoWriter.write("}");

    } catch (IOException exception) {
      failExecution(exception);
    }
  }
}
