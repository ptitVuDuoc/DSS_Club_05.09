package com.dss.dssClub.adapter;

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
import com.dss.dssClub.R;
import com.dss.dssClub.listen.OnLoadMoreListener;
import com.dss.dssClub.model.ListGift;

import java.util.ArrayList;
import java.util.List;

public class ListGiftAdapter extends RecyclerView.Adapter<ListGiftAdapter.ViewHolder>{

    private Context mContext;
    private List<ListGift> mListGift = new ArrayList<>();
    private ListGiftAdapter.OnStickerListener mItemListener;
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

    public ListGiftAdapter setOnItemListGiftListener(ListGiftAdapter.OnStickerListener listener) {
        mItemListener = listener;
        return this;
    }

    public ListGiftAdapter(RecyclerView recyclerView, Context context, List<ListGift> list) {
        this.mContext = context;
        this.mListGift = list;
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

    @Override
    public int getItemViewType(int position) {
        return mListGift.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public ListGiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_list_gift, parent, false);
        return new ListGiftAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txtNameProduct.setText(mListGift.get(position).getTenQuaTang());
        String scores ="-"+ mListGift.get(position).getSoDiemDoi().toString() + " " + "điểm";
        holder.txtScores.setText(scores);

        Glide.with(mContext)
                .load(mListGift.get(position).getLinkAnh())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.viewLoadImage.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.viewLoadImage.setVisibility(View.GONE);
                        holder.imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemListener != null){
                    mItemListener.onItemClickListener(position);
                }
            }
        });

        holder.btnChangeReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemListener != null){
                    mItemListener.onRewardClickListener(position);
                }
            }
        });

    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return mListGift == null? 0 : mListGift.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtNameProduct,txtScores;
        private LinearLayoutCompat btnChangeReward;
        private LinearLayout viewLoadImage;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_product);
            txtNameProduct = itemView.findViewById(R.id.txt_name_product);
            txtScores = itemView.findViewById(R.id.txt_scores);
            btnChangeReward = itemView.findViewById(R.id.btn_change_reward);
            viewLoadImage = itemView.findViewById(R.id.view_load_image);

        }
    }

    public interface OnStickerListener {
        void onItemClickListener(int position);
        void onRewardClickListener(int position);
    }
    
}
