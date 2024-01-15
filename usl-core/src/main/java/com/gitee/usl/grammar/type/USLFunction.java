package com.gitee.usl.grammar.type;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author hongda.li
 */
public interface USLFunction extends Callable<USLObject>, Runnable {

    String getName();

    USLObject call(Map<String, Object> env);

    USLObject call(Map<String, Object> env,
                   USLObject arg1);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15,
                   USLObject arg16);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15,
                   USLObject arg16,
                   USLObject arg17);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15,
                   USLObject arg16,
                   USLObject arg17,
                   USLObject arg18);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15,
                   USLObject arg16,
                   USLObject arg17,
                   USLObject arg18,
                   USLObject arg19);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15,
                   USLObject arg16,
                   USLObject arg17,
                   USLObject arg18,
                   USLObject arg19,
                   USLObject arg20);

    USLObject call(Map<String, Object> env,
                   USLObject arg1,
                   USLObject arg2,
                   USLObject arg3,
                   USLObject arg4,
                   USLObject arg5,
                   USLObject arg6,
                   USLObject arg7,
                   USLObject arg8,
                   USLObject arg9,
                   USLObject arg10,
                   USLObject arg11,
                   USLObject arg12,
                   USLObject arg13,
                   USLObject arg14,
                   USLObject arg15,
                   USLObject arg16,
                   USLObject arg17,
                   USLObject arg18,
                   USLObject arg19,
                   USLObject arg20,
                   USLObject... args);

}
