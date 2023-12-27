import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.kernel.domain.ResourceParam;

import java.util.Date;

/**
 * @author hongda.li
 */
public class Main {
    public static void main(String[] args) {
//        USLRunner runner = new USLRunner();
//        runner.start();
//
//        System.out.println(runner.run(new ResourceParam("Test.js")).getData());

        IntWrapper wrapper = new IntWrapper(ArrayUtil.isEmpty(args) ? 1 : args.length + 1);
        Object[] params = new Object[wrapper.get()];
        params[NumberConstant.ZERO] = new Date();
        while (wrapper.get() != 1) {
            params[params.length - wrapper.get() + 1] = args[params.length - wrapper.get()];
            wrapper.decrement();
        }

        System.out.println(ArrayUtil.toString(params));
    }
}
