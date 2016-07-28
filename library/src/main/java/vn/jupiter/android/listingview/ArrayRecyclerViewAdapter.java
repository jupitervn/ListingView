package vn.jupiter.android.listingview;

import android.content.Context;

import java.util.List;

/**
 * Adapter that is backed with a list of items.
 *
 * Created by Jupiter (vu.cao.duy@gmail.com) on 5/27/16.
 */
public class ArrayRecyclerViewAdapter<E> extends ItemSpecRecyclerViewAdapter<List<E>> {
    private final Object lock = new Object();

    public ArrayRecyclerViewAdapter(Context context, ItemSpecManager itemSpecManager) {
        super(context, itemSpecManager);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(E item) {
        synchronized (lock) {
            items.add(item);
            notifyItemInserted(items.size());
        }
    }

    public void removeItem(E item) {
        int index = items.indexOf(item);
        if (index > -1) {
            removeItemAtIndex(index);
        }
    }

    public void removeItemAtIndex(int index) {
        if (index >= 0 && index < items.size()) {
            synchronized (lock) {
                items.remove(index);
            }
            notifyItemRemoved(index);
        }
    }


    @Override
    public E getItemAtPosition(int position) {
        return items.get(position);
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
