package compiler;

import ast.trees.DeclarationTree;
import ast.trees.FunctionDeclarationTree;
import ast.trees.ProgramTree;
import codegen.CodeGenerator;
import codegen.program.Program;
import constrain.Constrainer;
import constrain.ConstraintException;
import lexer.ILexer;
import lexer.Lexception;
import lexer.Lexer;
import parser.Parser;
import parser.SyntaxErrorException;
import visitor.PrintVisitor;

public class Compiler {

  private String sourceFile;
  private PrintVisitor printVisitor;
  private ProgramTree ast;

  public Compiler(String sourceFile) {
    this.sourceFile = sourceFile;
    this.printVisitor = new PrintVisitor();
  }

  private String heading(String title) {
    int left = (76 - title.length()) / 2;
    int right = (76 - title.length()) / 2;

    if (left + right + title.length() != 76) {
      right++;
    }

    String leftHeader = String.format("%" + left + "s", "").replace(" ", "-");
    String rightHeader = String.format("%" + right + "s", "").replace(" ", "-");

    return String.format("%s%s%s", leftHeader, title, rightHeader);
  }

  private void printTrees(String title) throws Exception {
    String treesTitle = String.format(" Intrinsic Trees (%s) ", title);
    String astTitle = String.format(" AST (%s) ", title);

    if (Constrainer.intType != null) {
      System.out.println(heading(treesTitle));

      printVisitor.visit((DeclarationTree) Constrainer.intType);
      printVisitor.visit((DeclarationTree) Constrainer.boolType);
      printVisitor.visit((FunctionDeclarationTree) Constrainer.readTree);
      printVisitor.visit((FunctionDeclarationTree) Constrainer.writeTree);
    }

    System.out.println(heading(astTitle));
    printVisitor.visit(this.ast);
    System.out.println();
  }

  public void compileProgram() {
    try {
      ILexer lexer = new Lexer(sourceFile);
      Parser parser = new Parser(lexer);

      this.ast = (ProgramTree) parser.execute();
      printTrees("AST");

      Constrainer constrainer = new Constrainer(ast, lexer);
      try {
        constrainer.execute();
      } catch (ConstraintException e) {
        System.err.println(e.getMessage());
        System.exit(1);
      }
      printTrees("Constrainer");

      CodeGenerator generator = new CodeGenerator(ast);
      Program program = generator.execute();
      printTrees("Code Generator");

      System.out.println(program);
      program.write(sourceFile.replace(".x", ".cod"));
    } catch (Lexception e) {
      e.printStackTrace();
    } catch (SyntaxErrorException e) {
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: java compiler.Compiler <file>");
      System.exit(1);
    }

    Compiler compiler = new Compiler(args[0]);
    compiler.compileProgram();
  }
}
