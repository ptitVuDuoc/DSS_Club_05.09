package com.example.admin.dss_project.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.dss_project.R;
import com.example.admin.dss_project.listen.OnLoadMoreListener;
import com.example.admin.dss_project.model.Notifi;

import java.util.ArrayList;
import java.util.List;

public class ListNotifiAdapter extends RecyclerView.Adapter<ListNotifiAdapter.ViewHolder>{

    private Context mContext;
    private List<Notifi> mListNotifi = new ArrayList<>();
    private OnStickerListener mItemListener;
    private RecyclerView recyclerView;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private int visibleThreshold = 2;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public ListNotifiAdapter setOnItemListGiftListener(OnStickerListener listener) {
        mItemListener = listener;
        return this;
    }

    public ListNotifiAdapter(RecyclerView recyclerView, Context context, List<Notifi> list) {
        this.mContext = context;
        this.mListNotifi = list;
        this.recyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

//    @Override
//    public int getItemViewType(int position) {
//        return mListNotifi.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }

    @Override
    public ListNotifiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_notifi, parent, false);
        return new ListNotifiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemListener != null){
                    mItemListener.onItemClickListener(mListNotifi.get(position));
                }
            }
        });

        holder.txtDateNotifi.setText(mListNotifi.get(position).getNgay());
        holder.txtTitleNotifi.setText(mListNotifi.get(position).getTieuDe());
        holder.txtContentNotifi.setText(mListNotifi.get(position).getNoiDung());
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mListNotifi == null? 0 : mListNotifi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitleNotifi,txtContentNotifi, txtDateNotifi;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitleNotifi = itemView.findViewById(R.id.txt_title_notifi);
            txtContentNotifi = itemView.findViewById(R.id.txt_content_notifi);
            txtDateNotifi = itemView.findViewById(R.id.txt_date_notifi);

        }
    }

    public interface OnStickerListener {
        void onItemClickListener(Notifi notifi);
    }
    
}
