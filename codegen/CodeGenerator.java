package codegen;

import java.util.Iterator;
import java.util.Stack;

import ast.AST;
import ast.trees.*;
import codegen.program.*;
import constrain.Constrainer;
import visitor.TreeVisitor;

public class CodeGenerator extends TreeVisitor {
  private AST tree;
  private Stack<Frame> frameSizes;
  private Program program;
  private int labelNumber;

  public CodeGenerator(AST tree) {
    this.tree = tree;
    this.frameSizes = new Stack<>();
    this.program = new Program();
  }

  public Program execute() throws Exception {
    this.tree.accept(this);

    return this.program;
  }

  private String createLabel(String label) {
    return String.format("%s<<%d>>", label, ++labelNumber);
  }

  private void openFrame() {
    this.frameSizes.push(new Frame());
  }

  private void closeFrame() {
    this.frameSizes.pop();
  }

  private int currentFrameSize() {
    return this.frameSizes.peek().getSize();
  }

  private void changeFrameSize(int n) {
    this.frameSizes.peek().change(n);
  }

  private void openBlock() {
    this.frameSizes.peek().openBlock();
  }

  private void closeBlock() {
    this.frameSizes.peek().closeBlock();
  }

  private int getTopBlockSize() {
    return this.frameSizes.peek().getTopBlockSize();
  }

  private void storeCode(Code code) {
    int change = Codes.getFrameSizeChange(
        code.getByteCode());
    this.program.store(code);

    if (change == Codes.UnknownChange) {
      changeFrameSize(-((NumOpCode) code).getN());
    } else {
      changeFrameSize(change);
    }
  }

  @Override
  public Object visit(ProgramTree node) throws Exception {
    String startLabel = createLabel("start");

    openFrame();

    storeCode(new LabelCode(Codes.ByteCodes.GOTO, startLabel));
    generateIntrinsicByteCodes();
    storeCode(new LabelCode(Codes.ByteCodes.LABEL, startLabel));

    node.getChild(0).accept(this);

    storeCode(new Code(Codes.ByteCodes.HALT));

    closeFrame();

    return null;
  }

  private void generateIntrinsicByteCodes() {
    String readLabel = "Read";
    String writeLabel = "Write";

    AST readTree = Constrainer.readTree;
    AST writeTree = Constrainer.writeTree;

    readTree.setLabel(readLabel);
    storeCode(new LabelCode(Codes.ByteCodes.LABEL, readLabel));
    storeCode(new Code(Codes.ByteCodes.READ));
    storeCode(new LabelCode(Codes.ByteCodes.RETURN, readLabel));

    writeTree.setLabel(writeLabel);
    storeCode(new LabelCode(Codes.ByteCodes.LABEL, writeLabel));

    String formal = ((IdentifierTree) writeTree.getChild(2).getChild(0).getChild(1)).getSymbol().getLexeme();
    storeCode(new VarOpCode(Codes.ByteCodes.LOAD, 0, formal));
    storeCode(new Code(Codes.ByteCodes.WRITE));
    storeCode(new LabelCode(Codes.ByteCodes.RETURN, writeLabel));
  }

  @Override
  public Object visit(BlockTree node) throws Exception {
    openBlock();

    visitChildren(node);

    storeCode(new NumOpCode(Codes.ByteCodes.POP, getTopBlockSize()));

    closeBlock();

    return null;
  }

  @Override
  public Object visit(DeclarationTree node) throws Exception {
    AST idTree = node.getChild(1);
    String id = ((IdentifierTree) idTree).getSymbol().getLexeme();

    node.setLabel(id);

    idTree.setFrameOffset(currentFrameSize());

    storeCode(new VarOpCode(Codes.ByteCodes.LIT, 0, id));

    return null;
  }

  @Override
  public Object visit(FunctionDeclarationTree node) throws Exception {
    AST idTree = node.getChild(1);
    AST formalsTree = node.getChild(2);

    String id = ((IdentifierTree) idTree).getSymbol().getLexeme();

    String label = createLabel(id);
    node.setLabel(label);

    String continueLabel = createLabel("continue");
    storeCode(new LabelCode(Codes.ByteCodes.GOTO, continueLabel));

    openFrame();
    storeCode(new LabelCode(Codes.ByteCodes.LABEL, label));

    for (AST formalDeclaration : formalsTree.getChildren()) {
      IdentifierTree formalIdTree = (IdentifierTree) (formalDeclaration.getChild(1));
      formalIdTree.setFrameOffset(currentFrameSize());
      formalDeclaration.setLabel(formalIdTree.getSymbol().getLexeme());

      changeFrameSize(1);
    }

    node.getChild(3).accept(this);

    storeCode(new VarOpCode(Codes.ByteCodes.LIT, 0, "DEFAULT-RETURN-VALUE"));
    storeCode(new LabelCode(Codes.ByteCodes.RETURN, label));

    closeFrame();
    storeCode(new LabelCode(Codes.ByteCodes.LABEL, continueLabel));

    return null;
  }

  @Override
  public Object visit(FormalsTree node) throws Exception {
    return null;
  }

  @Override
  public Object visit(IntTypeTree node) throws Exception {
    return null;
  }

  @Override
  public Object visit(BoolTypeTree node) throws Exception {
    return null;
  }

  @Override
  public Object visit(IfTree node) throws Exception {
    String elseLabel = createLabel("else");
    String continueLabel = createLabel("continue");

    node.getChild(0).accept(this);
    storeCode(new LabelCode(Codes.ByteCodes.FALSEBRANCH, elseLabel));

    node.getChild(1).accept(this);
    storeCode(new LabelCode(Codes.ByteCodes.GOTO, continueLabel));

    storeCode(new LabelCode(Codes.ByteCodes.LABEL, elseLabel));
    node.getChild(2).accept(this);

    storeCode(new LabelCode(Codes.ByteCodes.LABEL, continueLabel));

    return null;
  }

  @Override
  public Object visit(WhileTree node) throws Exception {
    String continueLabel = createLabel("continue");
    String whileLabel = createLabel("while");

    storeCode(new LabelCode(Codes.ByteCodes.LABEL, whileLabel));

    node.getChild(0).accept(this);
    storeCode(new LabelCode(Codes.ByteCodes.FALSEBRANCH, continueLabel));

    node.getChild(1).accept(this);
    storeCode(new LabelCode(Codes.ByteCodes.GOTO, whileLabel));

    storeCode(new LabelCode(Codes.ByteCodes.LABEL, continueLabel));

    return null;
  }

  @Override
  public Object visit(ReturnTree node) throws Exception {
    node.getChild(0).accept(this);

    String functionLabel = node.getDecoration().getLabel();
    storeCode(new LabelCode(Codes.ByteCodes.RETURN, functionLabel));

    return null;
  }

  @Override
  public Object visit(AssignmentTree node) throws Exception {
    IdentifierTree idTree = (IdentifierTree) node.getChild(0);
    int offset = idTree.getDecoration().getChild(1).getFrameOffset();

    node.getChild(1).accept(this);

    storeCode(new VarOpCode(Codes.ByteCodes.STORE, offset, idTree.getSymbol().getLexeme()));

    return null;
  }

  @Override
  public Object visit(CallTree node) throws Exception {
    String functionLabel = node.getChild(0).getDecoration().getLabel();
    int argumentCount = node.getChild(1).getChildCount();

    Iterator<AST> iterator = node.getChild(1).getChildren().iterator();
    while (iterator.hasNext()) {
      iterator.next().accept(this);
    }

    storeCode(new NumOpCode(Codes.ByteCodes.ARGS, argumentCount));
    storeCode(new LabelCode(Codes.ByteCodes.CALL, functionLabel));

    return null;
  }

  @Override
  public Object visit(ActualArgumentsTree node) throws Exception {
    return null;
  }

  @Override
  public Object visit(RelOpTree node) throws Exception {
    String operator = node.getSymbol().getLexeme();

    node.getChild(0).accept(this);
    node.getChild(1).accept(this);

    storeCode(new LabelCode(Codes.ByteCodes.BOP, operator));

    return null;
  }

  @Override
  public Object visit(AddOpTree node) throws Exception {
    String operator = node.getSymbol().getLexeme();

    node.getChild(0).accept(this);
    node.getChild(1).accept(this);

    storeCode(new LabelCode(Codes.ByteCodes.BOP, operator));

    return null;
  }

  @Override
  public Object visit(MultOpTree node) throws Exception {
    String operator = node.getSymbol().getLexeme();

    node.getChild(0).accept(this);
    node.getChild(1).accept(this);

    storeCode(new LabelCode(Codes.ByteCodes.BOP, operator));

    return null;
  }

  @Override
  public Object visit(IntTree node) throws Exception {
    int number = Integer.parseInt(node.getSymbol().getLexeme());

    storeCode(new NumOpCode(Codes.ByteCodes.LIT, number));

    return null;
  }

  @Override
  public Object visit(IdentifierTree node) throws Exception {
    int offset = node.getDecoration().getChild(1).getFrameOffset();

    storeCode(new VarOpCode(Codes.ByteCodes.LOAD, offset, node.getSymbol().getLexeme()));

    return null;
  }

  @Override
  public Object visit(IterationTree node) throws Exception {
    if (!(node instanceof IterationTree)) {
        throw new IllegalArgumentException("Invalid node type for IterationTree");
    }
    node.getChild(0).accept(this); 
    node.getChild(0).getChild(1).accept(this);

    storeCode(new Code(Codes.ByteCodes.BOP, "-")); 

    String startLoopLabel = createLabel("startLoop");
    String endLoopLabel = createLabel("endLoop");
    
    storeCode(new LabelCode(Codes.ByteCodes.LABEL, startLoopLabel));
    
    node.getChild(1).accept(this);
    storeCode(new Code(Codes.ByteCodes.LIT, "1"));
    storeCode(new Code(Codes.ByteCodes.BOP, "-"));
    storeCode(new Code(Codes.ByteCodes.DUP));
    storeCode(new Code(Codes.ByteCodes.LIT, "0"));
    storeCode(new Code(Codes.ByteCodes.BOP, ">="));
    storeCode(new LabelCode(Codes.ByteCodes.FALSEBRANCH, endLoopLabel));

    storeCode(new LabelCode(Codes.ByteCodes.GOTO, startLoopLabel));

    storeCode(new LabelCode(Codes.ByteCodes.LABEL, endLoopLabel));
    storeCode(new Code(Codes.ByteCodes.POP));

    return null;
}

  @Override
  public Object visit(RangeTree node) throws Exception {
    return null;
  }
}
