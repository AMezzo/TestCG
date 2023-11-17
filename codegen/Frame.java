package codegen;

import java.util.Stack;

public class Frame {
  private int size;
  private Stack<Block> blocks;

  public Frame() {
    this.blocks = new Stack<>();

    openBlock();
  }

  public int getSize() {
    return this.size;
  }

  public void openBlock() {
    this.blocks.push(new Block());
  }

  public int closeBlock() {
    int topBlockSize = this.blocks.peek().getSize();

    this.size -= topBlockSize;
    this.blocks.pop();

    return topBlockSize;
  }

  public void change(int n) {
    this.blocks.peek().change(n);
    this.size += n;
  }

  public int getTopBlockSize() {
    return this.blocks.peek().getSize();
  }
}
