package com.gitee.usl.cli;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
public class CliMain {
    public static void main(String[] args) throws Exception {
        // 创建终端
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .encoding(StandardCharsets.UTF_8)
                .build();

        // 读取终端输入
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        // 输出欢迎语
        terminal.writer().append("Welcome USL-Runner-1");

        // 提示符
        String prompt = "USL > ";
        while (true) {
            terminal.writer().append("\n");
            terminal.flush();

            final String line;
            try {
                line = lineReader.readLine(prompt);
            } catch (UserInterruptException e) {
                // user cancelled line with Ctrl+C
                continue;
            } catch (EndOfFileException e) {
                // user cancelled application with Ctrl+D or kill
                break;
            } catch (Throwable t) {
                throw new Exception("Could not read from command line.", t);
            }
            if (line == null) {
                continue;
            }
            // 获取输入,根据输入做对应的操作,CommandCall
            System.out.println(line);
        }
    }
}
