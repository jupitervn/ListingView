package vn.jupiter.android.listingview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.jupiter.android.listingview.ArrayRecyclerViewAdapter;
import vn.jupiter.android.listingview.ItemSpec;
import vn.jupiter.android.listingview.ItemSpecManager;
import vn.jupiter.android.listingview.SimpleOnItemClickListener;
import vn.jupiter.android.listingview.SwipeLayout;

public class DemoActivity extends AppCompatActivity {

    private SwipeLayout listingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        listingView = (SwipeLayout) findViewById(R.id.listing_view);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ItemSpecManager itemSpecManager = new ItemSpecManager().addItemSpec(0, new ItemSpec<ItemVH, String>() {
            @Override
            public ItemVH onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                return new ItemVH(new TextView(parent.getContext()));
            }

            @Override
            public boolean canHandleViewType(Object item, int position) {
                return true;
            }

            @Override
            public void onBindViewHolder(String item, int position, ItemVH holder) {
                holder.textview.setText(position + " " + item);
            }
        });
        ArrayRecyclerViewAdapter<String> adapter = new ArrayRecyclerViewAdapter(this, itemSpecManager);
        recyclerView.setAdapter(adapter);
        List<String> arrays = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            arrays.add("Item ");
        }
        adapter.setItems(arrays);
        recyclerView.addOnItemTouchListener(new SimpleOnItemClickListener(recyclerView,
                new SimpleOnItemClickListener.OnItemClickListener() {
                    @Override
                    public boolean performItemClick(RecyclerView parent, View view, int position, long id) {

                        return true;
                    }
                }));
        listingView.setOnSwipeDownActionListener(new SwipeLayout.OnSwipeDownActionListener() {
            @Override
            public void onSwipeDownStarted() {

            }

            @Override
            public boolean shouldStartRefreshing(SwipeLayout swipeLayout, View targetView, float offset) {
                Log.d("ListingView", "should start refreshing " + offset);
//                return offset >= 250;
                return false;
            }

            @Override
            public void onRefreshingStatusChange(boolean isRefreshing) {
                if (isRefreshing) {
                    listingView.setRefreshing(false);
                } else {
                    recyclerView.setTranslationY(0);
                }
            }

            @Override
            public void onSwipeDownInProgress(SwipeLayout swipeLayout, View targetView, float offset) {
                Log.d("ListingView", "On swipe down in progress " + offset);
                targetView.setTranslationY(offset);
            }
        });
    }

    private static class ItemVH extends RecyclerView.ViewHolder {

        public TextView textview;

        public ItemVH(View itemView) {
            super(itemView);
            textview = ((TextView) itemView);
        }

    }

}
