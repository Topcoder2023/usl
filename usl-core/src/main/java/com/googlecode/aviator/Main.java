package com.googlecode.aviator;

import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import com.googlecode.aviator.utils.Env;

import java.io.IOException;
import java.util.Map;

/**
 * AviatorScript bootstrap
 *
 * @author dernnis(killme2008@gmail.com)
 *
 */
public class Main {

  public static void main(final String[] args) throws Exception {
    if (args == null || args.length < 1) {
      System.err.println("Usage: java com.googlecode.aviator.Main [file] [args]");
      System.err.println("     : java com.googlecode.aviator.Main -e [script]");
      System.err.println("     : java com.googlecode.aviator.Main -v");
      System.exit(1);
    }

    AviatorEvaluator.getInstance().setOption(Options.FEATURE_SET, Feature.getFullFeatures());
    AviatorEvaluator.getInstance()
        .setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());

    final String cmdOrPath = args[0];
    if (cmdOrPath.equals("-v") || cmdOrPath.equals("--version")) {
      System.out.println("AviatorScript " + AviatorEvaluator.VERSION);
      System.exit(0);
    } else if (cmdOrPath.equals("-e") || cmdOrPath.equals("--execute")) {
      if (args.length < 2) {
        System.err.println("Usage: java com.googlecode.aviator.Main -e [script]");
        System.exit(1);
      }
      String script = args[1];
      String[] remainArgs = getRemainArgs(args, 2);
      Expression exp = AviatorEvaluator.getInstance().compile(script);
      System.out.println(exp.execute(newEnv(exp, null, remainArgs)));
    } else {
      String[] remainArgs = getRemainArgs(args, 1);
      Expression exp = AviatorEvaluator.getInstance().compileScript(cmdOrPath);

      System.out.println(exp.execute(newEnv(exp, cmdOrPath, remainArgs)));
    }

  }

  private static Map<String, Object> newEnv(final Expression exp, final String abPath,
      final String[] args) throws IOException {
    final Env exports = new Env();
    final Map<String, Object> module =
        exp.newEnv("exports", exports, "path", abPath, "dir", getFileDir(abPath));
    Map<String, Object> env = exp.newEnv("__MODULE__", module, "exports", exports, "ARGV", args);
    return env;
  }

  private static String getFileDir(final String abPath) throws IOException {
    return abPath != null
        ? AviatorEvaluator.getInstance().tryFindScriptFile(abPath).getAbsoluteFile().getParent()
        : null;
  }

  private static String[] getRemainArgs(final String[] args, final int startPos) {
    String[] remainArgs = new String[args.length - startPos];
    System.arraycopy(args, startPos, remainArgs, 0, remainArgs.length);
    return remainArgs;
  }
}
