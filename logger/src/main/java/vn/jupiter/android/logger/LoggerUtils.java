package vn.jupiter.android.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 7/25/16.
 */
public class LoggerUtils {
    private static final int CALL_STACK_INDEX = 0;
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

    public static String getTagFromClassName() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length <= CALL_STACK_INDEX) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX]);
    }

    private static String createStackElementTag(StackTraceElement stackTraceElement) {
        String tag = stackTraceElement.getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        return tag.substring(tag.lastIndexOf('.') + 1);
    }

}
