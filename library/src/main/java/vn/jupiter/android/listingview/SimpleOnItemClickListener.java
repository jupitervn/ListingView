package vn.jupiter.android.listingview;

import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Take idea from `twoway-view` of lucasr.<br>
 *
 * @see <a href="https://github.com/lucasr/twoway-view/blob/master/core/src/main/java/org/lucasr/twowayview/ClickItemTouchListener.java">ClickItemTouchListener.java</a>
 *
 * Created by Jupiter (vu.cao.duy@gmail.com) on 12/1/15.
 */
public class SimpleOnItemClickListener implements RecyclerView.OnItemTouchListener {
    private final GestureDetectorCompat mGestureDetector;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public SimpleOnItemClickListener(RecyclerView hostView, OnItemClickListener onItemClickListener) {
        this(hostView, onItemClickListener, null);
    }

    public SimpleOnItemClickListener(RecyclerView hostView, OnItemClickListener onItemClickListener,
            OnItemLongClickListener onItemLongClickListener) {
        mGestureDetector = new GestureDetectorCompat(hostView.getContext(), new ItemClickGestureListener(hostView));
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private boolean isAttachedToWindow(RecyclerView hostView) {
        if (Build.VERSION.SDK_INT >= 19) {
            return hostView.isAttachedToWindow();
        } else {
            return (hostView.getHandler() != null);
        }
    }

    private boolean hasAdapter(RecyclerView hostView) {
        return (hostView.getAdapter() != null);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        if (!isAttachedToWindow(recyclerView) || !hasAdapter(recyclerView)) {
            return false;
        }

        mGestureDetector.onTouchEvent(event);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        // We can silently track tap and and long presses by silently
        // intercepting touch events in the host RecyclerView.
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemClickGestureListener implements GestureDetector.OnGestureListener {
        private final RecyclerView mHostView;
        private View mTargetChild;

        public ItemClickGestureListener(RecyclerView hostView) {
            mHostView = hostView;
        }

        public void dispatchSingleTapUpIfNeeded(MotionEvent event) {
            // When the long press hook is called but the long press listener
            // returns false, the target child will be left around to be
            // handled later. In this case, we should still treat the gesture
            // as potential item click.
            if (mTargetChild != null) {
                onSingleTapUp(event);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            mTargetChild = mHostView.findChildViewUnder(x, y);
            return (mTargetChild != null);
        }

        @Override
        public void onShowPress(MotionEvent event) {
            if (mTargetChild != null) {
                mTargetChild.setPressed(true);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            boolean handled = false;

            if (mTargetChild != null) {
                mTargetChild.setPressed(false);

                final int position = mHostView.getChildPosition(mTargetChild);
                final long id = mHostView.getAdapter().getItemId(position);
                if (!mTargetChild.dispatchTouchEvent(event)) {
                    handled = performItemClick(mHostView, mTargetChild, position, id);
                }
                mTargetChild = null;
            }

            return handled;
        }

        @Override
        public boolean onScroll(MotionEvent event, MotionEvent event2, float v, float v2) {
            if (mTargetChild != null) {
                mTargetChild.setPressed(false);
                mTargetChild = null;

                return true;
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (mTargetChild == null) {
                return;
            }

            final int position = mHostView.getChildLayoutPosition(mTargetChild);
            final long id = mHostView.getAdapter().getItemId(position);
            final boolean handled = performItemLongClick(mHostView, mTargetChild, position, id);

            if (handled) {
                mTargetChild.setPressed(false);
                mTargetChild = null;
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private boolean performItemClick(RecyclerView mHostView, View mTargetChild, int position, long id) {
        if (onItemClickListener != null) {
            return onItemClickListener.performItemClick(mHostView, mTargetChild, position, id);
        }
        return false;
    }

    private boolean performItemLongClick(RecyclerView mHostView, View mTargetChild, int position, long id) {
        if (onItemLongClickListener != null) {
            return onItemLongClickListener.performItemLongClick(mHostView, mTargetChild, position, id);
        }
        return false;
    }

    public interface OnItemClickListener {
        boolean performItemClick(RecyclerView parent, View view, int position, long id);
    }

    public interface OnItemLongClickListener {
        boolean performItemLongClick(RecyclerView parent, View view, int position, long id);
    }
}
