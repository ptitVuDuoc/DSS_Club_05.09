package com.example.admin.dss_project.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.dss_project.R;
import com.example.admin.dss_project.listen.OnLoadMoreListener;
import com.example.admin.dss_project.model.ListGiftRegistered;
import com.example.admin.dss_project.model.ListSeriaRegistered;

import java.util.ArrayList;

public class HistoryRewardAdapter extends RecyclerView.Adapter<HistoryRewardAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ListGiftRegistered> mListHistory = new ArrayList<>();
    private OnItemListener mItemListener;
    private RecyclerView mRecyclerView ;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public HistoryRewardAdapter setOnItemListener(OnItemListener listener) {
        mItemListener = listener;
        return this;
    }

    public HistoryRewardAdapter(RecyclerView recyclerView, Context context, ArrayList<ListGiftRegistered> list) {
        mContext = context;
        mListHistory = list;
        mRecyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }

            }
        });

    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mListHistory.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_list_gift_registered, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtNameProduct.setText(mListHistory.get(position).getTenQuaTang());
        holder.txtDate.setText(mListHistory.get(position).getNgayDangKy());
        holder.txtStatus.setText(mListHistory.get(position).getTinhTrang());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemListener != null){
                    mItemListener.onItemClickListener(mListHistory.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListHistory == null? 0 : mListHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameProduct;
        private TextView txtDate;
        private TextView txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNameProduct = (TextView) itemView.findViewById(R.id.txt_name_tiem);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);

        }
    }

    public interface OnItemListener {
        void onItemClickListener(ListGiftRegistered  listGiftRegistered);
    }
}
