package vn.jupiter.android.logger;

import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Logger implementation that will log everything to logcat.
 *
 * Created by Jupiter (vu.cao.duy@gmail.com) on 4/27/16.
 */
public class AndroidLogger extends Logger {

    private static final int MAX_LOG_LENGTH = 4000;
    private final String customPrefix;
    private String customTag;

    public AndroidLogger() {
        this(null, null);
    }

    /**
     * Create new instance of Android logger.
     * @param customTag defines the custom log tag.
     * @param customPrefix defines the custom prefix for log message.
     */
    public AndroidLogger(@Nullable String customTag, @Nullable String customPrefix) {
        this.customTag = customTag;
        this.customPrefix = customPrefix;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (message.length() <= MAX_LOG_LENGTH) {
            doPrintLog(priority, tag, message);
        } else {
            for (int i = 0, length = message.length(); i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    String part = message.substring(i, end);
                    doPrintLog(priority, tag, part);
                    i = end;
                } while (i < newline);
            }
        }
    }

    @SuppressWarnings("WrongConstant")
    private void doPrintLog(int priority, String tag, String message) {
        if (customPrefix != null) {
            message = customPrefix + " " + message;
        }
        Log.println(convertLogPriority(priority), tag, message);
    }

    private int convertLogPriority(int priority) {
        switch (priority) {
            case Logger.DEBUG_LEVEL:
                priority = Log.DEBUG;
                break;
            case Logger.INFO_LEVEL:
                priority = Log.INFO;
                break;
            case Logger.ERROR_LEVEL:
                priority = Log.ERROR;
                break;
            case Logger.VERBOSE_LEVEL:
                priority = Log.VERBOSE;
                break;
            case Logger.WARN_LEVEL:
                priority = Log.WARN;
                break;
            default:
                priority = Log.DEBUG;
        }
        return priority;
    }

    @Override
    protected String getTag() {
        return customTag != null ? customTag : LoggerUtils.getTagFromClassName();
    }

}
