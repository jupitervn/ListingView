package vn.jupiter.android.logger;

import java.util.Collections;
import java.util.List;

/**
 * Logger that pass the log to all of its children.<br>
 *
 * Created by Jupiter (vu.cao.duy@gmail.com) on 7/25/16.
 */
public class CompositeLogger extends Logger {

    private List<Logger> loggers = Collections.emptyList();
    private String logTag = null;

    public CompositeLogger(List<Logger> loggers, String logTag) {
        this.loggers = loggers;
        this.logTag = logTag;
    }

    public void addLogger(Logger logger) {
        synchronized (loggers) {
            loggers.add(logger);
        }
    }

    public void removeLogger(Logger logger) {
        synchronized (loggers) {
            loggers.remove(logger);
        }
    }

    public void removeAllLoggers() {
        synchronized (loggers) {
            loggers.clear();
        }
    }

    public int getLoggerCount() {
        return loggers.size();
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        for (Logger logger : loggers) {
            logger.log(priority, tag, message, t);
        }
    }

    @Override
    protected String getTag() {
        return logTag == null ? LoggerUtils.getTagFromClassName() : null;
    }
}
