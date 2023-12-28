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
        USLRunner runner = new USLRunner();
        runner.start();

        System.out.println(runner.run(new ResourceParam("Test.js")).getData());
    }
}
