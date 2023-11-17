package codegen;

import java.util.HashMap;
import java.util.Map;

public class Codes {
  public static enum ByteCodes {
    HALT,
    GOTO,
    WRITE,
    LABEL,
    // POP n (pop n entries from the runtime stack)
    POP,
    // FALSEBRANCH <label> (pop the stack; if 0 goto <label> else continue)
    FALSEBRANCH,
    // STORE n <id> (pop the stack; store the value at offset n from start of
    // current frame)
    STORE,
    // LOAD n <id> (get the value at offset n from start of current frame; push to
    // stack)
    LOAD,
    // LIT n (pushes n to stack)
    // LIT 0 <id> (push 0 to stack to reserve a slot for variable <id>)
    LIT,
    // ARGS n (create a new frame n from the top of the stack)
    ARGS,
    // CALL <label> (branch to LABEL <label>)
    CALL,
    // RETURN <label> (pop the stack; pop the frame; push the popped value)
    RETURN,
    // BOP <op> (pop the stack twice, perform <op>, push the result)
    BOP,
    // READ (get a value from the user; push to)
    READ
  }

  private static Map<ByteCodes, Integer> frameSizeChanges;
  public static final int UnknownChange = Integer.MAX_VALUE;

  static {
    frameSizeChanges = new HashMap<>();

    frameSizeChanges.put(ByteCodes.HALT, 0);
    frameSizeChanges.put(ByteCodes.GOTO, 0);
    frameSizeChanges.put(ByteCodes.WRITE, 0);
    frameSizeChanges.put(ByteCodes.LABEL, 0);
    frameSizeChanges.put(ByteCodes.POP, UnknownChange);
    frameSizeChanges.put(ByteCodes.FALSEBRANCH, -1);
    frameSizeChanges.put(ByteCodes.STORE, -1);
    frameSizeChanges.put(ByteCodes.LOAD, 1);
    frameSizeChanges.put(ByteCodes.LIT, 1);
    frameSizeChanges.put(ByteCodes.ARGS, UnknownChange);
    frameSizeChanges.put(ByteCodes.CALL, 1);
    frameSizeChanges.put(ByteCodes.RETURN, -1);
    frameSizeChanges.put(ByteCodes.BOP, -1);
    frameSizeChanges.put(ByteCodes.READ, 1);
  }

  public static int getFrameSizeChange(ByteCodes code) {
    return frameSizeChanges.get(code);
  }
}
