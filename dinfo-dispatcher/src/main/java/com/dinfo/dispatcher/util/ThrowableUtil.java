package com.dinfo.dispatcher.util;

import java.io.PrintWriter;
import java.io.Writer;

import static com.dinfo.dispatcher.util.ObjectUtil.isNull;
import static com.dinfo.dispatcher.util.ObjectUtil.nonEmpty;


/**
 * @author yangxf
 */
public class ThrowableUtil {

    // ============ throwable to string ============

    public static String errToString(Throwable t) {
        return errToString(t, null);
    }

    public static String errToString(Throwable t, String msg) {
        try (JStringWriter w = new JStringWriter();
             PrintWriter p = new PrintWriter(w)) {
            if (nonEmpty(msg))
                w.write(msg + '\n');
            t.printStackTrace(p);
            return w.toString();
        }
    }

    public static final class JStringWriter extends Writer {

        private StringBuilder buf;

        public JStringWriter() {
            lock = buf = new StringBuilder();
        }

        public JStringWriter(int capacity) {
            if (capacity < 0)
                throw new IllegalArgumentException("Negative buffer size");
            lock = buf = new StringBuilder(capacity);
        }

        @Override
        public void write(int c) {
            buf.append(c);
        }

        @Override
        public void write(char[] cbuf) {
            if (cbuf.length > 0)
                buf.append(cbuf);
        }

        @Override
        public void write(String str) {
            if (nonEmpty(str))
                buf.append(str);
        }

        @Override
        public void write(String str, int off, int len) {
            if (nonEmpty(str))
                buf.append(str.substring(off, off + len));
        }

        @Override
        public void write(char[] cbuf, int off, int len) {
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                    ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return;
            }
            buf.append(cbuf, off, len);
        }

        @Override
        public Writer append(CharSequence csq) {
            if (isNull(csq))
                write("null");
            else
                write(csq.toString());
            return this;
        }

        @Override
        public Writer append(CharSequence csq, int start, int end) {
            CharSequence cs = (csq == null ? "null" : csq);
            write(cs.subSequence(start, end).toString());
            return this;
        }

        @Override
        public Writer append(char c) {
            write(c);
            return this;
        }

        @Override
        public String toString() {
            return buf.toString();
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }

}
