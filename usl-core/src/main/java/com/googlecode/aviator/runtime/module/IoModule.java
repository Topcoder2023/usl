package com.googlecode.aviator.runtime.module;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import com.googlecode.aviator.annotation.Function;
import com.googlecode.aviator.annotation.Import;
import com.googlecode.aviator.annotation.ImportScope;

/**
 * A simple io module for aviator
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
@Import(scopes = {ImportScope.Static}, ns = "io")
public class IoModule {

  private static final int INIT_BUFFER_SIZE = 32 * 1024;
  private static final int BUFFER_SIZE = 8096;

  /**
   * A function to return java.io.File instance by path.
   *
   * @param path
   * @return
   */
  public static final File file(final String path) {
    return new File(path);
  }

  public static boolean exists(final File file) {
    return file.exists();
  }

  @Function(rename = "list_files")
  public static File[] files(final File file) {
    return file.listFiles();
  }

  public static final URL resource(final String name) {
    return Thread.currentThread().getContextClassLoader().getResource(name);
  }

  @Function(rename = "input_stream")
  public static InputStream inputStream(final File file) throws IOException {
    return new FileInputStream(file);
  }

  @Function(rename = "input_stream")
  public static InputStream inputStream(final URL url) throws IOException {
    return url.openStream();
  }

  @Function(rename = "output_stream")
  public static FileOutputStream outputStream(final File file) throws FileNotFoundException {
    return new FileOutputStream(file);
  }

  public int read(final InputStream in) throws IOException {
    return in.read();
  }


  public void write(final OutputStream out, final int b) throws IOException {
    out.write(b);
  }

  public static BufferedReader reader(final File file) throws IOException {
    return reader(file, Charset.defaultCharset().name());
  }

  public static BufferedReader reader(final File file, final String charsetName)
      throws IOException {
    return reader(inputStream(file), charsetName);
  }

  public static BufferedReader reader(final InputStream in) throws IOException {
    return new BufferedReader(new InputStreamReader(in, Charset.defaultCharset().name()));
  }


  public static BufferedReader reader(final InputStream in, final String charsetName)
      throws IOException {
    return new BufferedReader(new InputStreamReader(in, Charset.forName(charsetName)));
  }


  public static BufferedWriter writer(final File file) throws IOException {
    return writer(file, Charset.defaultCharset().name());
  }

  public static BufferedWriter writer(final File file, final String charsetName)
      throws IOException {
    return writer(outputStream(file), charsetName);
  }

  public static BufferedWriter writer(final OutputStream out) throws IOException {
    return writer(out, Charset.defaultCharset().name());
  }

  public static BufferedWriter writer(final OutputStream out, final String charsetName)
      throws IOException {
    return new BufferedWriter(new OutputStreamWriter(out, Charset.forName(charsetName)));
  }

  /**
   * slurp function to read file fully as a string.
   *
   * @param path
   * @return
   * @throws IOException
   */
  public static String slurp(final String path) throws IOException {
    return slurp(file(path));
  }

  public static String slurp(final String path, final String charset) throws IOException {
    return slurp(file(path), charset);
  }

  public static String slurp(final File file) throws IOException {
    return slurp(file, Charset.defaultCharset().name());
  }

  public static boolean delete(final File file) {
    return file.delete();
  }

  public static String slurp(final File file, final String charset) throws IOException {
    byte[] data = new byte[(int) file.length()];
    try (InputStream in = inputStream(file)) {
      int read = in.read(data);
      assert (read == data.length);
    }
    return new String(data, charset);
  }

  public static String slurp(final URL file) throws IOException {
    return slurp(file, Charset.defaultCharset().name());
  }

  private static byte[] resizeBuffer(final byte[] buffer, final int newSize, final int len) {
    byte[] newBuffer = new byte[newSize];
    System.arraycopy(buffer, 0, newBuffer, 0, len);
    return newBuffer;
  }

  public static String slurp(final URL file, final String charset) throws IOException {
    byte[] data = new byte[BUFFER_SIZE];
    byte[] buffer = new byte[INIT_BUFFER_SIZE];
    int destPos = 0;
    try (InputStream in = inputStream(file)) {
      int read = 0;
      while ((read = in.read(data)) == data.length) {
        while (destPos + read > buffer.length) {
          buffer = resizeBuffer(buffer, buffer.length + INIT_BUFFER_SIZE, destPos);
        }
        System.arraycopy(data, 0, buffer, destPos, read);
        destPos += data.length;
      }
      if (read > 0) {
        while (destPos + read > buffer.length) {
          buffer = resizeBuffer(buffer, buffer.length + INIT_BUFFER_SIZE, destPos);
        }
        System.arraycopy(data, 0, buffer, destPos, read);
        destPos += data.length;
      }
    }
    return new String(data, 0, destPos, charset);
  }

  /**
   * spit function to write a string fully to file.
   *
   * @param path
   * @return
   * @throws IOException
   */
  public static void spit(final String path, final String content) throws IOException {
    spit(file(path), content);
  }

  public static void spit(final String path, final String content, final String charset)
      throws IOException {
    spit(file(path), content, charset);
  }

  public static void spit(final File file, final String content) throws IOException {
    spit(file, content, Charset.defaultCharset().name());
  }

  public static void spit(final File file, final String content, final String charset)
      throws IOException {
    byte[] data = content.getBytes(Charset.forName(charset));
    try (OutputStream out = outputStream(file)) {
      out.write(data);
    }
  }

  /**
   * cast a file into a sequence of text lines in file.
   *
   * @param file
   * @return
   * @throws IOException
   */
  @Function(rename = "line_seq")
  public static LineSequence seq(final File file) throws IOException {
    return new LineSequence(reader(file));
  }

  @Function(rename = "line_seq")
  public static LineSequence seq(final BufferedReader reader) {
    return new LineSequence(reader);
  }

  public static void close(final Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        // ignore
      }
    }
  }
}
