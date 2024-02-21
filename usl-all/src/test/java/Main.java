import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.ResourceParam;

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
