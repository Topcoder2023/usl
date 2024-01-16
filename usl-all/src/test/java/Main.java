import com.gitee.usl.Runner;
import com.gitee.usl.kernel.domain.ResourceParam;

/**
 * @author hongda.li
 */
public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.start();

        System.out.println(runner.run(new ResourceParam("Test.js")).getData());
    }
}
