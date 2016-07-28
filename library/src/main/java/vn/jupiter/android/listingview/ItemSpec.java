package vn.jupiter.android.listingview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 5/27/16.
 */
public interface ItemSpec<VH extends RecyclerView.ViewHolder, E> {

    VH onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent);

    boolean canHandleViewType(Object item, int position);

    void onBindViewHolder(E item, int position, VH holder);
}
