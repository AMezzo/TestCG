package constrain;

import java.util.Stack;

import ast.AST;
import ast.ISymbolTree;
import ast.trees.*;
import lexer.ILexer;
import visitor.TreeVisitor;

public class Constrainer extends TreeVisitor {

  private Table symbolTable;
  private Stack<AST> functions;
  private ILexer lexer;
  private AST tree;

  public static AST intType, boolType, binaryType, charType,
      readId, writeId, readTree, writeTree;

  public Constrainer(AST tree, ILexer lexer) {
    this.tree = tree;
    this.lexer = lexer;
  }

  public void execute() throws Exception {
    this.symbolTable = new Table();
    this.functions = new Stack<>();

    symbolTable.beginScope();
    tree.accept(this);
  }

  private AST getType(AST tree) {
    if (tree.getClass() == IntTypeTree.class) {
      return intType;
    } else {
      return boolType;
    }
  }

  private void buildIntrinsicTrees() throws Exception {
    intType = new DeclarationTree().addChild(new IntTypeTree()).addChild(
        new IdentifierTree(lexer.generateAnonymousToken("<<int>>")));
    intType.getChild(1).setDecoration(intType);

    boolType = new DeclarationTree().addChild(new BoolTypeTree()).addChild(
        new IdentifierTree(lexer.generateAnonymousToken("<<bool>>")));
    boolType.getChild(1).setDecoration(boolType);

    readId = new IdentifierTree(lexer.generateAnonymousToken("read"));
    writeId = new IdentifierTree(lexer.generateAnonymousToken("write"));

    readTree = new FunctionDeclarationTree()
        .addChild(new IntTypeTree())
        .addChild(readId)
        .addChild(new FormalsTree())
        .addChild(new BlockTree());
    readTree.accept(this);

    writeTree = new FunctionDeclarationTree()
        .addChild(new IntTypeTree())
        .addChild(writeId)
        .addChild(new FormalsTree());

    AST writeParameterDeclaration = new DeclarationTree()
        .addChild(new IntTypeTree())
        .addChild(new IdentifierTree(lexer.generateAnonymousToken("dummyFormal")));

    writeTree.getChild(2).addChild(writeParameterDeclaration);
    writeTree.addChild(new BlockTree());
    writeTree.accept(this);
  }

  @Override
  public Object visit(ProgramTree tree) throws Exception {
    buildIntrinsicTrees();

    tree.getChild(0).accept(this);

    return null;
  }

  @Override
  public Object visit(BlockTree tree) throws Exception {
    symbolTable.beginScope();
    visitChildren(tree);
    symbolTable.endScope();

    return null;
  }

  @Override
  public Object visit(DeclarationTree tree) {
    AST identifierTree = tree.getChild(1);
    symbolTable.put(((ISymbolTree) identifierTree).getSymbol(), tree);

    AST typeTree = getType(tree.getChild(0));
    identifierTree.setDecoration(typeTree);

    return null;
  }

  @Override
  public Object visit(FunctionDeclarationTree tree) throws Exception {
    this.functions.push(tree);

    AST identifierTree = tree.getChild(1);
    this.symbolTable.put(((ISymbolTree) identifierTree).getSymbol(), tree);

    AST returnType = getType(tree.getChild(0));
    tree.getChild(0).setDecoration(returnType);

    this.symbolTable.beginScope();
    visitChildren(tree.getChild(2));

    tree.getChild(3).accept(this);

    this.symbolTable.endScope();
    this.functions.pop();

    return null;
  }

  @Override
  public Object visit(FormalsTree tree) {
    return null;
  }

  @Override
  public Object visit(IntTypeTree tree) {
    return null;
  }

  @Override
  public Object visit(BoolTypeTree tree) {
    return null;
  }

  private void constrainGuardExpressionTree(AST tree) throws Exception {
    if (tree.getChild(0).accept(this) != boolType) {
      throw new ConstraintException(ConstrainerErrors.BadConditional, tree);
    }

    tree.getChild(1).accept(this);

    if (tree.getChildCount() == 3) {
      tree.getChild(2).accept(this);
    }

  }

  @Override
  public Object visit(IfTree tree) throws Exception {
    constrainGuardExpressionTree(tree);
    return null;
  }

  @Override
  public Object visit(WhileTree tree) throws Exception {
    constrainGuardExpressionTree(tree);
    return null;
  }

  @Override
  public Object visit(ReturnTree tree) throws Exception {
    if (this.functions.empty()) {
      throw new ConstraintException(ConstrainerErrors.ReturnNotInFunction, tree);
    }

    AST currentFunction = this.functions.peek();
    tree.setDecoration(currentFunction);

    AST returnType = currentFunction.getChild(0).getDecoration();
    if (tree.getChild(0).accept(this) != returnType) {
      throw new ConstraintException(ConstrainerErrors.BadReturnExpression, tree);
    }

    return null;
  }

  @Override
  public Object visit(AssignmentTree tree) throws Exception {
    AST identifierTree = tree.getChild(0);
    AST identifierDeclaration = (AST) this.symbolTable.get(((ISymbolTree) identifierTree).getSymbol());

    identifierTree.setDecoration(identifierDeclaration);

    AST typeTree = identifierDeclaration.getChild(1).getDecoration();

    if (tree.getChild(1).accept(this) != typeTree) {
      throw new ConstraintException(ConstrainerErrors.BadAssignmentType, tree);
    }

    return null;
  }

  @Override
  public Object visit(CallTree tree) throws Exception {
    visitChildren(tree);

    AST callTreeIdentifier = tree.getChild(0);

    AST functionDeclaration = (AST) this.symbolTable.get(
        ((ISymbolTree) callTreeIdentifier).getSymbol());

    if (functionDeclaration.getClass() != FunctionDeclarationTree.class) {
      throw new ConstraintException(ConstrainerErrors.CallingNonFunction, tree);
    }

    AST functionReturnType = functionDeclaration.getChild(0).getDecoration();
    tree.setDecoration(functionReturnType);

    callTreeIdentifier.setDecoration(functionDeclaration);

    checkActualArguments(tree.getChild(1), functionDeclaration.getChild(2));

    return functionReturnType;
  }

  private void checkActualArguments(AST actualArguments, AST formalParameters) throws Exception {
    if (actualArguments.getChildCount() != formalParameters.getChildCount()) {
      throw new ConstraintException(ConstrainerErrors.NumberActualsFormalsDiffer, tree);
    }

    for (int childIndex = 0; childIndex < actualArguments.getChildCount()
        && childIndex < formalParameters.getChildCount(); childIndex++) {

      AST actualType = (AST) actualArguments.getChild(childIndex).accept(this);
      AST formalType = formalParameters.getChild(childIndex).getChild(1).getDecoration();

      if (actualType != formalType) {
        throw new ConstraintException(ConstrainerErrors.ActualFormalTypeMismatch, tree);
      }
    }
  }

  @Override
  public Object visit(ActualArgumentsTree tree) {
    return null;
  }

  @Override
  public Object visit(RelOpTree tree) throws Exception {
    AST left = tree.getChild(0);
    AST right = tree.getChild(1);

    if (left.accept(this) != right.accept(this)) {
      throw new ConstraintException(ConstrainerErrors.TypeMismatchInExpression, tree);
    }

    tree.setDecoration(boolType);

    return boolType;
  }

  private AST constrainBinaryMathematicalOperation(AST tree) throws Exception {
    AST left = tree.getChild(0);
    AST right = tree.getChild(1);

    AST leftType = (AST) left.accept(this);
    AST rightType = (AST) right.accept(this);

    if (leftType != rightType) {
      throw new ConstraintException(ConstrainerErrors.TypeMismatchInExpression, tree);
    }

    tree.setDecoration(leftType);

    return leftType;
  }

  @Override
  public Object visit(AddOpTree tree) throws Exception {
    return constrainBinaryMathematicalOperation(tree);
  }

  @Override
  public Object visit(MultOpTree tree) throws Exception {
    return constrainBinaryMathematicalOperation(tree);
  }

  @Override
  public Object visit(IntTree tree) {
    tree.setDecoration(intType);

    return intType;
  }

  @Override
  public Object visit(IdentifierTree tree) {
    AST declarationTree = (AST) this.symbolTable.get(((ISymbolTree) tree).getSymbol());
    tree.setDecoration(declarationTree);

    return declarationTree.getChild(1).getDecoration();
  }

  @Override
  public Object visit(IterationTree tree) throws Exception {
    tree.getChild(0).accept(this);
    tree.getChild(1).accept(this);

    return null;
  }

  @Override
  public Object visit(RangeTree tree) throws Exception {
    AST left = (AST) tree.getChild(0).accept(this);
    AST right = (AST) tree.getChild(1).accept(this);

    if (left != intType || right != intType) {
      throw new ConstraintException(ConstrainerErrors.BadRangeExpression, tree);
    }

    return null;
  }

}