package vn.jupiter.android.listingview.exception;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 5/27/16.
 */
public class ContentNotFoundException extends Exception {

    public ContentNotFoundException() {
    }

    public ContentNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public ContentNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ContentNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
