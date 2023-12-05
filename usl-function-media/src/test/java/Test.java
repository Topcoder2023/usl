import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import ws.schild.jave.EncoderException;

import java.io.File;

/**
 * @author hongda.li
 */
public class Test {
    public static void main(String[] args) throws EncoderException {
        USLRunner runner = new USLRunner();
        runner.start();

        Result<Object> run = runner.run(new Param().setScript("ffmpeg.transform(source, target)")
                .addContext("source", new File("D:\\1.mp4"))
                .addContext("target", new File("D:\\1.wav")));
    }
}
