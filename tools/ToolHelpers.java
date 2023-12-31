package tools;

public class ToolHelpers {
  public static String getIndentation(int indentationSize) {
    StringBuffer buffer = new StringBuffer();

    for (int indent = 0; indent < indentationSize; indent++) {
      buffer.append("  ");
    }

    return buffer.toString();
  }

  public static String getNewLines(int numberOfNewLines) {
    StringBuffer buffer = new StringBuffer();

    for (int newLine = 0; newLine < numberOfNewLines; newLine++) {
      buffer.append(System.lineSeparator());
    }

    return buffer.toString();
  }

  public static String endAndIndent(int numberOfNewLines, int indentationSize) {
    return getNewLines(numberOfNewLines) + getIndentation(indentationSize);
  }

  public static String getComment(String content) {
    return String.format("// %s", content);
  }

  public static String getAutoGeneratedWarning() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("/**");
    buffer.append(getNewLines(1));
    buffer.append(" * This file is automatically generated!");
    buffer.append(getNewLines(1));
    buffer.append(" * Do not manually update! (Use the ToolRunner to regenerate.)");
    buffer.append(getNewLines(1));
    buffer.append(" **/");

    return buffer.toString();
  }

  public static void failExecution(Exception exception) {
    System.err.println(String.format(
        "The TokenTool %s failed to run:",
        exception.getStackTrace()[2].getClassName()));
    exception.printStackTrace();
    System.exit(1);
  }
}
