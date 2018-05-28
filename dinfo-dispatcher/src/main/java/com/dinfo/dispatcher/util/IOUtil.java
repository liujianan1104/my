package com.dinfo.dispatcher.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import static com.dinfo.dispatcher.util.ObjectUtil.checkNullAndThrow;
import static com.dinfo.dispatcher.util.SystemUtil.WINDOWS;
import static com.dinfo.dispatcher.util.ThrowableUtil.errToString;


/**
 * Uncompleted!
 *
 * @author yangxf
 * @since 2.0
 */
public class IOUtil {

    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final int DEFAULT_CAPACITY = 1 << 20;

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
        try (FileInputStream fis = new FileInputStream(source)) {
            if (nio)
                return copyByNIO(fis, out);
            return copy(fis, out);
        } catch (Exception e) {
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
        try (BufferedInputStream bis = new BufferedInputStream(in);
             BufferedOutputStream bos = new BufferedOutputStream(out)
        ) {
            byte[] buf = new byte[DEFAULT_CAPACITY];
            int i;
            while ((i = bis.read(buf)) > 0)
                bos.write(buf, 0, i);
        } catch (IOException e) {
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
