package vn.jupiter.android.listingview;

import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 5/27/16.
 */
public class ItemSpecManager {

    protected SparseArrayCompat<ItemSpec> supportedItemSpecs = new SparseArrayCompat<>();
    private OnItemSpecChangedListener onItemSpecListChanged;

    public ItemSpecManager addItemSpec(int viewType, ItemSpec delegate) {
        supportedItemSpecs.put(viewType, delegate);
        notifyChangeListener();
        return this;
    }

    public void removeItemSpec(ItemSpec delegate) {
        int index = supportedItemSpecs.indexOfValue(delegate);
        if (index > -1) {
            supportedItemSpecs.removeAt(index);
        }
        notifyChangeListener();
    }

    public void removeItemSpecOfViewType(int viewType) {
        supportedItemSpecs.remove(viewType);
        notifyChangeListener();
    }

    public int getViewType(Object itemAtPosition, int position) {
        int size = supportedItemSpecs.size();
        ItemSpec itemSpec = null;
        for (int i = 0; i < size; i++) {
            itemSpec = supportedItemSpecs.valueAt(i);
            if (itemSpec.canHandleViewType(itemAtPosition, position)) {
                return supportedItemSpecs.keyAt(i);
            }
        }
        throw new RuntimeException("Cannot find item spec that can handle item at " + position);
    }

    public RecyclerView.ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int viewType) {
        ItemSpec itemSpec = getItemSpecByType(viewType);
        if (itemSpec != null) {
            return itemSpec.onCreateViewHolder(layoutInflater, parent);
        } else {
            return null;
        }
    }

    public void onBindViewHolder(Object itemAtPosition, int position, RecyclerView.ViewHolder holder) {
        ItemSpec itemSpec = getItemSpecByType(holder.getItemViewType());
        if (itemSpec != null) {
            itemSpec.onBindViewHolder(itemAtPosition, position, holder);
        } else {
            throw new RuntimeException("Cannot find item spec that can handle item at " + position);
        }
    }

    @Nullable
    private ItemSpec getItemSpecByType(int itemViewType) {
        return supportedItemSpecs.get(itemViewType);
    }

    public void setOnItemSpecListChanged(OnItemSpecChangedListener onItemSpecListChanged) {
        this.onItemSpecListChanged = onItemSpecListChanged;
    }

    private void notifyChangeListener() {
        if (onItemSpecListChanged != null) {
            onItemSpecListChanged.onItemSpecChanged();
        }
    }

    public interface OnItemSpecChangedListener {
        void onItemSpecChanged();
    }
}
