package com.dinfo.dispatcher.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import static com.dinfo.dispatcher.util.ObjectUtil.checkNullAndThrow;
import static com.dinfo.dispatcher.util.ObjectUtil.nonNull;
import static com.dinfo.dispatcher.util.SystemUtil.WINDOWS;
import static com.dinfo.dispatcher.util.ThrowableUtil.errToString;


/**
 * Uncompleted!
 * @author yangxf
 * @since 2.0
 */
public class IOUtil {

    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final int DEFAULT_CAPACITY = 12 << 1;

    /**
     * File to output
     *
     * @see IOUtil#download(File, OutputStream)
     */
    public static boolean download(String filePath, OutputStream out) {
        return download(new File(filePath), out);
    }

    /**
     * File to output
     *
     * @see IOUtil#download(File, OutputStream, boolean)
     */
    public static boolean download(String filePath, OutputStream out, boolean nio) {
        return download(new File(filePath), out, nio);
    }

    /**
     * File to output
     *
     * @see IOUtil#download(String, OutputStream, boolean)
     */
    public static boolean download(File source, OutputStream out) {
        return download(source, out, false);
    }

    /**
     * File to output
     *
     * @see IOUtil#copy(InputStream, OutputStream)
     * @see IOUtil#copyByNIO(InputStream, OutputStream)
     */
    public static boolean download(File source, OutputStream out, boolean nio) {
        checkNullAndThrow(source);
        try {
            if (nio)
                return copyByNIO(new FileInputStream(source), out);
            return copy(new FileInputStream(source), out);
        } catch (FileNotFoundException e) {
            logger.error(errToString(e));
            return false;
        }
    }

    /**
     * Input to output by new IO.
     *
     * @return false if throw, otherwise true
     */
    public static boolean copyByNIO(InputStream in, OutputStream out) {
        try (ReadableByteChannel inChannel = Channels.newChannel(in);
             WritableByteChannel outChannel = Channels.newChannel(out)
        ) {
            ByteBuffer buf = ByteBuffer.allocate(DEFAULT_CAPACITY);

            for (; inChannel.read(buf) > -1; ) {
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }
        } catch (Exception e) {
            logger.error(errToString(e));
            return false;
        }

        return true;
    }

    /**
     * Input to output
     *
     * @return false if throw, otherwise true
     */
    public static boolean copy(InputStream in, OutputStream out) {
        checkNullAndThrow(in, out);
        try (InputStreamReader isr = new InputStreamReader(in, DEFAULT_CHARSET);
             BufferedReader br = new BufferedReader(isr);
             OutputStreamWriter osw = new OutputStreamWriter(out, DEFAULT_CHARSET);
             PrintWriter pw = new PrintWriter(osw)
        ) {

            String line;
            if (WINDOWS)
                while (nonNull(line = br.readLine()))
                    writelnWin(pw, line);
            else
                while (nonNull(line = br.readLine()))
                    writelnUnix(pw, line);

        } catch (Exception e) {
            logger.error(errToString(e));
            return false;
        }
        return true;
    }

    public static void writeln(Writer w, String line) throws IOException {
        if (WINDOWS)
            writelnWin(w, line);
        else
            writelnUnix(w, line);
    }

    private static void writelnWin(Writer w, String line) throws IOException {
        w.write(line + "\r\n");
    }

    private static void writelnUnix(Writer w, String line) throws IOException {
        w.write(line + "\n");
    }
}
