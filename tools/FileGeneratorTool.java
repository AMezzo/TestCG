package tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public abstract class FileGeneratorTool implements Iterator<String> {
  private List<String> lines;
  private int index;

  public abstract void regenerateSourceFile();

  public FileGeneratorTool(Path filePath) {
    this.index = 0;

    try {
      this.lines = Files.readAllLines(filePath);
    } catch (IOException e) {
      ToolHelpers.failExecution(e);
    }
  }

  @Override
  public boolean hasNext() {
    return this.index < this.lines.size();
  }

  @Override
  public String next() {
    if (!hasNext()) {
      return "";
    }

    return this.lines.get(this.index++);
  }
}
