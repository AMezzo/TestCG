package codegen.program;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Program {
  private List<Code> codes;

  public Program() {
    this.codes = new ArrayList<>();
  }

  public void store(Code code) {
    this.codes.add(code);
  }

  public void write(String fileName) {
    try (FileWriter writer = new FileWriter(fileName)) {
      writer.write(this.toString());
    } catch (IOException e) {
      System.err.println(String.format("Error while creating byte code file %s", fileName));
    }
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    Iterator<Code> iterator = this.codes.iterator();
    while (iterator.hasNext()) {
      buffer.append(
          String.format("%s\n", iterator.next().toString()));
    }

    return buffer.toString().trim();
  }
}
