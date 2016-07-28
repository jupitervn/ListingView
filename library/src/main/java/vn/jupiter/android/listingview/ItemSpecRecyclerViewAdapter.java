package vn.jupiter.android.listingview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 5/26/16.
 */
public abstract class ItemSpecRecyclerViewAdapter<C> extends RecyclerView.Adapter
        implements ItemSpecManager.OnItemSpecChangedListener {
    protected C items;
    private ItemSpecManager itemSpecManager;
    private LayoutInflater layoutInflater;

    public ItemSpecRecyclerViewAdapter(Context context, ItemSpecManager itemSpecManager) {
        this.itemSpecManager = itemSpecManager;
        itemSpecManager.setOnItemSpecListChanged(this);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return itemSpecManager.getViewType(getItemAtPosition(position), position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return itemSpecManager.createViewHolder(layoutInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        itemSpecManager.onBindViewHolder(getItemAtPosition(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public abstract Object getItemAtPosition(int position);

    @Override
    public void onItemSpecChanged() {
        notifyDataSetChanged();
    }

    public void setItems(@NonNull C items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
