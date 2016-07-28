package vn.jupiter.android.logger;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 4/27/16.
 */
public class EmptyLogger extends Logger {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        //Do nothing
    }

    @Override
    protected String getTag() {
        return null;
    }
}
