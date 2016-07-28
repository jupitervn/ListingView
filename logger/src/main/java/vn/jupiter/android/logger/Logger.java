package vn.jupiter.android.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Base logger for mediakit's components.
 * Created by Jupiter (vu.cao.duy@gmail.com) on 4/27/16.
 */
public abstract class Logger {
    protected static final int VERBOSE_LEVEL = 0x1;
    protected static final int DEBUG_LEVEL = 0x2;
    protected static final int INFO_LEVEL = 0x3;
    protected static final int WARN_LEVEL = 0x4;
    protected static final int ERROR_LEVEL = 0x5;

    public void d(String message, Object... optionals) {
        prepareLog(DEBUG_LEVEL, null, message, optionals);
    }
    public void v(String message, Object... optionals) {
        prepareLog(VERBOSE_LEVEL, null, message, optionals);
    }
    public void i(String message, Object... optionals) {
        prepareLog(INFO_LEVEL, null, message, optionals);
    }
    public void w(String message, Object... optionals) {
        prepareLog(WARN_LEVEL, null, message, optionals);
    }
    public void w(Throwable t, String message, Object... optionals) {
        prepareLog(WARN_LEVEL, t, message, optionals);
    }
    public void e(Throwable t, String message, Object... optionals) {
        prepareLog(ERROR_LEVEL, t, message, optionals);
    }

    private void prepareLog(int priority, Throwable t, String message, Object... args) {
        if (message != null && message.length() == 0) {
            message = null;
        }
        if (message == null) {
            if (t == null) {
                return; // Swallow message if it's null and there's no throwable.
            }
            message = getStackTraceString(t);
        } else {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (t != null) {
                message += "\n" + getStackTraceString(t);
            }
        }

        log(priority, getTag(), message, t);
    }
    private String getStackTraceString(Throwable t) {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    protected abstract void log(int priority, String tag, String message, Throwable t);

    protected abstract String getTag();
}
