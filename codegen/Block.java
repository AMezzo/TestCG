package codegen;

public class Block {
  private int size;

  public int getSize() {
    return this.size;
  }

  public void change(int n) {
    this.size += n;
  }
}
