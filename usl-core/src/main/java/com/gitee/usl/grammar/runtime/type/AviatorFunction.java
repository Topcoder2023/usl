package com.gitee.usl.grammar.runtime.type;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author hongda.li
 */
public interface AviatorFunction extends Callable<AviatorObject>, Runnable {

    String getName();

    AviatorObject call(Map<String, Object> env);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15,
                       AviatorObject arg16);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15,
                       AviatorObject arg16,
                       AviatorObject arg17);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15,
                       AviatorObject arg16,
                       AviatorObject arg17,
                       AviatorObject arg18);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15,
                       AviatorObject arg16,
                       AviatorObject arg17,
                       AviatorObject arg18,
                       AviatorObject arg19);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15,
                       AviatorObject arg16,
                       AviatorObject arg17,
                       AviatorObject arg18,
                       AviatorObject arg19,
                       AviatorObject arg20);

    AviatorObject call(Map<String, Object> env,
                       AviatorObject arg1,
                       AviatorObject arg2,
                       AviatorObject arg3,
                       AviatorObject arg4,
                       AviatorObject arg5,
                       AviatorObject arg6,
                       AviatorObject arg7,
                       AviatorObject arg8,
                       AviatorObject arg9,
                       AviatorObject arg10,
                       AviatorObject arg11,
                       AviatorObject arg12,
                       AviatorObject arg13,
                       AviatorObject arg14,
                       AviatorObject arg15,
                       AviatorObject arg16,
                       AviatorObject arg17,
                       AviatorObject arg18,
                       AviatorObject arg19,
                       AviatorObject arg20,
                       AviatorObject... args);

}
