COMPILE_DIR=target
SRC_DIR=.
SOURCE_FILE=sources
JUNIT_JAR=junit-platform-console-standalone-1.9.3.jar
JUNIT_JAR_LOCATION=lib/$(JUNIT_JAR)
TEST_TAR=assignment-2b-tests.tar
ASSIGNMENT_GRADER=https://raw.githubusercontent.com/sfsu-csc-413-fall-2023/grader-visitor/main/

clean:
	@echo "Cleaning project workspace..."
	@find . -name "*.class" -type f -delete
	@rm -rf $(COMPILE_DIR)
	@rm -f $(SOURCE_FILE)
	@rm -f $(TEST_TAR)

all: clean
	@echo "Compiling..."
	@find $(SRC_DIR) -name "*.java" -not -path "./tests/*" > $(SOURCE_FILE)
	@javac -d $(COMPILE_DIR) @$(SOURCE_FILE)
	@rm $(SOURCE_FILE)

compile-simple: all
	@java -cp $(COMPILE_DIR) compiler.Compiler xsamples/simple.x

compile-simple-image: all
	@java -cp $(COMPILE_DIR) compiler.Compiler xsamples/simple.x -image

constrainer-fail: all
	@java -cp $(COMPILE_DIR) compiler.Compiler xsamples/constraint_failure.x

constrainer-succeed: all
	@java -cp $(COMPILE_DIR) compiler.Compiler xsamples/constraint_success.x

# Cleans then compiles project including tests
all-tests: clean
	@echo "Compiling for testing..."
	@find . -name "*.java" > $(SOURCE_FILE)
	@javac -d $(COMPILE_DIR) -cp $(COMPILE_DIR):$(JUNIT_JAR_LOCATION):. @$(SOURCE_FILE)
	@rm $(SOURCE_FILE)

# Runs all of the tests
test-all: tools all-tests
	@echo "Running tests..."
	@java -jar $(JUNIT_JAR_LOCATION) -cp $(COMPILE_DIR) --disable-banner --include-classname=.* --scan-classpath

# Runs a single test class provided as an argument to the makefile:
# make test TEST_NAME=tests.packageName.TestClassName
test: tools all-tests
	@echo "Running $(TEST_NAME)..."
	@java -jar $(JUNIT_JAR_LOCATION) -cp $(COMPILE_DIR) -c $(TEST_NAME) --disable-banner

# Runs a single test (method in a test class)
# For a @Test:
# make test METHOD_NAME=tests.packageName.TestClassName#methodName
# For a @ParameterizedTest, the test method's parameters must be provided (example):
# tests.lexer.LexerTest#testBuiltInOperators\(java.lang.String,lexer.daos.TokenKind,int,int\)
test-method: tools all-tests
	@echo "Running $(METHOD_NAME)..."
	@java -jar $(JUNIT_JAR_LOCATION) -cp $(COMPILE_DIR) -m $(METHOD_NAME)

# Updates or creates the SymbolTable class and TokenKind enum
tools: clean
	@echo "Regenerating files..."
	@javac -d $(COMPILE_DIR) tools/ToolRunner.java
	@java -cp $(COMPILE_DIR) tools.ToolRunner
	@rm -rf $(COMPILE_DIR)

visitor: clean
	@javac -d $(COMPILE_DIR) tools/visitor/CreateVisitor.java
	@echo "java -cp $(COMPILE_DIR) tools.visitor.CreateVisitor pathToVisitor"

# Retrieves the test tar from the grader repo, and replaces the local tests directory
update-tests: clean
	@rm -rf tests
	@wget $(ASSIGNMENT_GRADER)/$(TARBALL)
	@tar -xf $(TARBALL)
	@rm -f $(TARBALL)